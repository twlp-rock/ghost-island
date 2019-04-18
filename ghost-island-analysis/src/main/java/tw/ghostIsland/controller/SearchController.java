package tw.ghostIsland.controller;

import tw.ghostIsland.model.SpotEntity;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.bson.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value="/search")
public class SearchController {
    private String MONGODB_URI = "mongodb://localhost:27017/ghost-island";

    private SparkSession sparkSession = SparkSession.builder()
            .master("local")
            .appName("MongoSparkConnectorIntro")
            .config("spark.mongodb.input.uri", MONGODB_URI + ".spot")
            .config("spark.mongodb.output.uri", MONGODB_URI + ".meta")
            .getOrCreate();

    private JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());
    private JavaMongoRDD<Document> rdd = MongoSpark.load(jsc);

    @RequestMapping(value="")
    public List<?> search(HttpServletRequest request){
        String word = request.getParameter("word");

        Dataset<SpotEntity> ds = rdd.toDS(SpotEntity.class);
        ds.createOrReplaceTempView("spot");

        Dataset<String> rows = sparkSession.sql("SELECT description, location FROM spot WHERE description like '%"+word+"%'").toJSON();

        return rows.collectAsList();
    }

}
