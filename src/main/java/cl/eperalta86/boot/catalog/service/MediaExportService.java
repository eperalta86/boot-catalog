package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.exception.BusinessException;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MediaExportService {

    public enum Format {
        CSV("csv", "text/csv"),
        JSON("json", "application/json");

        private final String extension;
        private final String contentType;

        Format(String extension, String contentType) {
            this.extension = extension;
            this.contentType = contentType;
        }

        public String extension() { return extension; }
        public String contentType() { return contentType; }

        public static Format fromString(String value) {
            if (value == null || value.isBlank()) {
                return CSV;
            }
            try {
                return Format.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Formato no soportado: " + value + ". Usar 'csv' o 'json'.");
            }
        }
    }

    public record ExportResult(Path file, String contentType, String filename) {}

    private final JobLauncher jobLauncher;
    private final Job csvExportJob;
    private final Job jsonExportJob;

    public MediaExportService(JobLauncher jobLauncher, Job csvExportJob, Job jsonExportJob) {
        this.jobLauncher = jobLauncher;
        this.csvExportJob = csvExportJob;
        this.jsonExportJob = jsonExportJob;
    }

    public ExportResult export(Format format) {
        Path outputFile;
        try {
            outputFile = Files.createTempFile("backlog-export-", "." + format.extension());
        } catch (IOException e) {
            throw new BusinessException("No se pudo crear el archivo temporal: " + e.getMessage());
        }

        JobParameters params = new JobParametersBuilder()
                .addString("outputPath", outputFile.toString())
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        Job job = (format == Format.CSV) ? csvExportJob : jsonExportJob;

        JobExecution execution;
        try {
            execution = jobLauncher.run(job, params);
        } catch (Exception e) {
            throw new BusinessException("Error al ejecutar el job de export: " + e.getMessage());
        }

        if (execution.getStatus() != BatchStatus.COMPLETED) {
            throw new BusinessException("El job terminó con estado: " + execution.getStatus());
        }

        String filename = "backlog-export." + format.extension();
        return new ExportResult(outputFile, format.contentType(), filename);
    }
}
