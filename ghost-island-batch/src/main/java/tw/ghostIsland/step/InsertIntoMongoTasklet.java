package tw.ghostIsland.step;

import tw.ghostIsland.model.SpotEntity;
import tw.ghostIsland.repository.SpotRepository;
import com.opencsv.CSVReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.io.FileReader;

@Service
public class InsertIntoMongoTasklet implements Tasklet {
    @Autowired
    SpotRepository spotRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        try{
            CSVReader reader = new CSVReader(new FileReader("./data-source/normalization.csv"));
            String[] line;

            while ((line = reader.readNext()) != null) {
                SpotEntity spotEntity = new SpotEntity(line[0], line[1], new GeoJsonPoint(Double.parseDouble(line[2]),Double.parseDouble(line[3])));
                spotRepository.save(spotEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return RepeatStatus.FINISHED;
    }
}
