package tw.ghostIsland.processor;

import tw.ghostIsland.model.OpenData;

import org.springframework.batch.item.ItemProcessor;

public class OpenDataItemProcessor implements ItemProcessor<OpenData, String> {
    @Override
    public String process(OpenData openData) throws Exception{
        if ( openData.getLongitude().isEmpty() || openData.getLatitude().isEmpty() ){
            return null;
        }
        if ( Double.parseDouble(openData.getLatitude()) > 90 ){
            return null;
        }
        String date = openData.getLocalTime().split(" ")[0];
        String time = openData.getLocalTime().split(" ")[1];
        int year = Integer.parseInt(date.substring(0,3))+1911;
        return year
                + "-" + date.substring(4,6)
                + "-" + date.substring(7, 9)
                + " " + time.substring(0,2)
                + ":" + time.substring(3, 5)
                + ":" + time.substring(6, 8)
                + "," + openData.getDescription()
                + "," + openData.getLongitude()
                + "," + openData.getLatitude();
    }
}
