package cl.eperalta86.boot.catalog.config;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.dto.MediaExportItem;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class MediaExportBatchConfig {

    // ---------- Reader: lee los MediaItem desde la DB de a 50 por página ----------
    @Bean
    @StepScope
    public RepositoryItemReader<MediaItem> mediaItemReader(MediaItemRepository repository) {
        return new RepositoryItemReaderBuilder<MediaItem>()
                .name("mediaItemReader")
                .repository(repository)
                .methodName("findAll")
                .pageSize(50)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    // ---------- Processor: convierte MediaItem → MediaExportItem (DTO plano) ----------
    @Bean
    public ItemProcessor<MediaItem, MediaExportItem> mediaItemProcessor() {
        return MediaExportItem::from;
    }

    // ---------- Writer CSV: genera un archivo .csv ----------
    @Bean
    @StepScope
    public FlatFileItemWriter<MediaExportItem> csvWriter(
            @Value("#{jobParameters['outputPath']}") String outputPath) {

        FieldExtractor<MediaExportItem> extractor = item -> new Object[]{
                item.id(), item.title(), item.platformName(), item.status(), item.releaseDate()
        };

        DelimitedLineAggregator<MediaExportItem> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(extractor);

        return new FlatFileItemWriterBuilder<MediaExportItem>()
                .name("csvWriter")
                .resource(new FileSystemResource(outputPath))
                .headerCallback(writer -> writer.write("id,title,platformName,status,releaseDate"))
                .lineAggregator(lineAggregator)
                .build();
    }

    // ---------- Writer JSON: genera un archivo .json ----------
    @Bean
    @StepScope
    public JsonFileItemWriter<MediaExportItem> jsonWriter(
            @Value("#{jobParameters['outputPath']}") String outputPath) {

        return new JsonFileItemWriterBuilder<MediaExportItem>()
                .name("jsonWriter")
                .resource(new FileSystemResource(outputPath))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .build();
    }

    // ---------- Step CSV ----------
    @Bean
    public Step csvExportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            RepositoryItemReader<MediaItem> mediaItemReader,
            ItemProcessor<MediaItem, MediaExportItem> mediaItemProcessor,
            FlatFileItemWriter<MediaExportItem> csvWriter) {

        return new StepBuilder("csvExportStep", jobRepository)
                .<MediaItem, MediaExportItem>chunk(50, transactionManager)
                .reader(mediaItemReader)
                .processor(mediaItemProcessor)
                .writer(csvWriter)
                .build();
    }

    // ---------- Step JSON ----------
    @Bean
    public Step jsonExportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            RepositoryItemReader<MediaItem> mediaItemReader,
            ItemProcessor<MediaItem, MediaExportItem> mediaItemProcessor,
            JsonFileItemWriter<MediaExportItem> jsonWriter) {

        return new StepBuilder("jsonExportStep", jobRepository)
                .<MediaItem, MediaExportItem>chunk(50, transactionManager)
                .reader(mediaItemReader)
                .processor(mediaItemProcessor)
                .writer(jsonWriter)
                .build();
    }

    // ---------- Jobs ----------
    @Bean
    public Job csvExportJob(JobRepository jobRepository, Step csvExportStep) {
        return new JobBuilder("csvExportJob", jobRepository)
                .start(csvExportStep)
                .build();
    }

    @Bean
    public Job jsonExportJob(JobRepository jobRepository, Step jsonExportStep) {
        return new JobBuilder("jsonExportJob", jobRepository)
                .start(jsonExportStep)
                .build();
    }
}
