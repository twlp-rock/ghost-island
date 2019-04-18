package tw.ghostIsland.config.job;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbstractBatchJobConfiguration {
    @Autowired
    protected JobBuilderFactory jobBuilders;
    @Autowired
    protected StepBuilderFactory stepBuilders;
}
