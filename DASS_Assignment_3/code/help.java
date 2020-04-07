import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class help {
    public static void main(String[] args) {
        JSONArray employeeList;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("employees.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            employeeList = (JSONArray) obj;
            System.out.println(employeeList);
            JSONObject employeeDetails = new JSONObject();
            employeeDetails.put("firstName", "ram");
            employeeDetails.put("lastName", "Gupta");
            employeeDetails.put("website", "howtodoinjava.com");

            JSONObject employeeObject = new JSONObject();
            employeeObject.put("employee", employeeDetails);

            //Second Employee
            JSONObject employeeDetails2 = new JSONObject();
            employeeDetails2.put("firstName", "Brian");
            employeeDetails2.put("lastName", "Schultz");
            employeeDetails2.put("website", "example.com");

            JSONObject employeeObject2 = new JSONObject();
            employeeObject2.put("employee", employeeDetails2);

            //Add employees to list
//        JSONArray employeeList = new JSONArray();
            employeeList.add(employeeObject);
            employeeList.add(employeeObject2);

            //Write JSON file
            try (FileWriter file = new FileWriter("employees.json")) {

                file.write(employeeList.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
