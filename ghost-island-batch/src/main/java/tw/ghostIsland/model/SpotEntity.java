package tw.ghostIsland.model;

import com.google.common.hash.Hashing;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "spot")
public class SpotEntity {
    @Id
    private String id;

    @Field("local_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date localTime;

    @Field("description")
    private String description;

    @Field("location")
    private GeoJsonPoint location;

    public SpotEntity(final String _localTime, final String _description, final GeoJsonPoint _location) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.description = _description;
            this.location = _location;
            this.localTime = sdf.parse(_localTime);
            String originalString = _localTime + _description + _location;
            this.id = Hashing.sha256()
                    .hashString(originalString, StandardCharsets.UTF_8)
                    .toString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
