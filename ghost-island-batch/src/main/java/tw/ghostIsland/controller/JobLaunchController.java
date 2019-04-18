package tw.ghostIsland.controller;

import tw.ghostIsland.util.Stopwatch;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/jobLauncher")
public class JobLaunchController implements ApplicationContextAware {

    private final JobLauncher asyncJobLauncher;
    private ApplicationContext applicationContext;

    @Autowired
    public JobLaunchController(JobLauncher asyncJobLauncher) {
        this.asyncJobLauncher = asyncJobLauncher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping
    public String service(HttpServletRequest request){
        Stopwatch stopwatch = new Stopwatch().createStarted();
        String jobName = null;
        try{
            jobName = request.getParameter("job");
            final Job jobBean = (Job) applicationContext.getBean(jobName);
            final JobParametersBuilder builder = new JobParametersBuilder();
            builder.addDate("uuid", new Date());
            final JobExecution jobExec = asyncJobLauncher.run(jobBean, builder.toJobParameters());
        }catch (Exception e){
            e.printStackTrace();
        }


        return "{\"jobName\":\""+jobName+"\", \"elapsed\": "+stopwatch.elapsed()+"}";
    }


}
