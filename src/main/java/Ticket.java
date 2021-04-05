import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ticket {

    private static final String ticketsArrayString = "tickets";
    private static final String departureDate = "departure_date";
    private static final String departureTime = "departure_time";
    private static final String arrivalDate = "arrival_date";
    private static final String arrivalTime = "arrival_time";

    //change path to tickets.json here

    private String filePathNew;
    Ticket (String filePathNew){
        this.filePathNew = filePathNew;
    }

    public String getFilePath() {
        return filePathNew;
    }

    public String getJSONString(String filePath) {
        File jsonFile = new File(filePath);
        String result = "";
        try {
            Scanner in = new Scanner(new FileReader(jsonFile));
            while (in.hasNext()) {
                result += in.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //I don't know why, but this json file starts from whitespace
        StringBuilder sb = new StringBuilder(result);
        return sb.substring(1);
    }

    public JSONArray getJSONArray(String jsonString, String arrayString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray array = (JSONArray) jsonObject.get(arrayString);
        return array;
    }

    public ArrayList<Long> getTimeList(JSONArray array) {
        ArrayList<Long> timeList = new ArrayList<Long>();
        for (Object object : array) {

            String departureTimeString = ((JSONObject) object).getString(departureDate) + " " +
                    ((JSONObject) object).getString(departureTime);
            String arrivalTimeString = ((JSONObject) object).getString(arrivalDate) + " " +
                    ((JSONObject) object).getString(arrivalTime);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
            try {

                Date departureDate = dateFormat.parse(departureTimeString);
                Date arrivalDate = dateFormat.parse(arrivalTimeString);
                long travelMinutes = ((arrivalDate.getTime() - departureDate.getTime()) / 60000);
                timeList.add(travelMinutes);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeList;
    }
    public Long getSumMinutes(ArrayList<Long> timeList){
        long sum = 0;
        for (long i : timeList) {
            sum += i;
        }
        return sum;
    }

    public Long getAvgMinutes(ArrayList<Long> timeList) {
        long sum = 0;
        for (long i : timeList) {
            sum += i;
        }
        long avgMinutes = sum / timeList.size();
        return avgMinutes;
    }
    public String get90Percentile (ArrayList<Long> timeList){
        Collections.sort(timeList);
        int numberOfElement = (int) (0.9 * timeList.size())-1;
        long result = timeList.get(numberOfElement);
        int hours = (int) result/60;
        int minutes = (int) result%60;
        String resultString = "new 90 percentile time is: " + hours + " hours and " + minutes % 60 + " minutes";
        return resultString;
    }

    public String getResult(long avgMinutes) {
        int avgHours = (int) avgMinutes / 60;
        String avgTime = "Average time is: " + avgHours + " hours and " + avgMinutes % 60 + " minutes";
        return avgTime;
    }

    public static void main(String[] args) {
        Ticket ticket = new Ticket(args[0]);
        String jsonString = ticket.getJSONString(ticket.getFilePath());
        JSONArray array = ticket.getJSONArray(jsonString, ticketsArrayString);
        ArrayList<Long> timeList = ticket.getTimeList(array);
        Long avgMinutes = ticket.getAvgMinutes(timeList);

        System.out.println(ticket.getResult(avgMinutes));
        System.out.println(ticket.get90Percentile(timeList));
    }

}
