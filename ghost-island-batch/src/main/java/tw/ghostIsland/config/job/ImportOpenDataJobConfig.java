package tw.ghostIsland.config.job;

import tw.ghostIsland.model.OpenData;
import tw.ghostIsland.processor.OpenDataItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ImportOpenDataJobConfig extends AbstractBatchJobConfiguration {
    private static final String JOB_NAME = "importOpenDataJob";
    private static final String STEP_NAME = "importOpenDataStep";

    @Autowired
    @Qualifier("insertIntoMongoTasklet")
    private Tasklet insertIntoMongoTasklet;

    @Bean(name = JOB_NAME)
    public Job job() {
        return jobBuilders.get(JOB_NAME)
                .start(step(stepBuilders))
                .next(insertMongoStep(stepBuilders))
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step step(StepBuilderFactory stepBuilders){
        return stepBuilders.get("normalizeStep")
                .<OpenData, String>chunk(10).reader(reader())
                .processor(processor()).writer(writer()).build();
    }

    @Bean
    public FlatFileItemReader<OpenData> reader() {
        return new FlatFileItemReaderBuilder<OpenData>()
                .name("openDataItemReader")
                .resource(new FileSystemResource("./data-source/raw.csv")).linesToSkip(1)
                .delimited().names(new String[] {"localTime", "description", "hurt", "type", "longitude", "latitude"})
                .targetType(OpenData.class).build();
    }

    @Bean
    public OpenDataItemProcessor processor(){
        return new OpenDataItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<String> writer() {
        return new FlatFileItemWriterBuilder<String>()
                .name("openDataItemWriter")
                .resource(new FileSystemResource(
                        "./data-source/normalization.csv"))
                .lineAggregator(new PassThroughLineAggregator<>()).build();
    }

    @Bean
    public Step insertMongoStep(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("insertIntoMongoStep")
                .tasklet(insertIntoMongoTasklet)
                .build();
    }
}
