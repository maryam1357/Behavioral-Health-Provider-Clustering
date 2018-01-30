import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
public class ManipulateData
{


    private int convertStringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double convertStringToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Hashtable<String,Hashtable<String,Coordinate>> readCoorData(String filePath)
    {
        Hashtable<String, Hashtable<String, Coordinate>> coordinateData = new Hashtable<>();
        try{
            BufferedReader buf = new BufferedReader(new FileReader(filePath));
            String lineJustFetched = null;
            String[] wordsArray;
            while(true)
            {
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null ){
                    break;
                }else{
                    Coordinate newCoorRow = new Coordinate();
                    wordsArray = lineJustFetched.split(",");
                    int columnNumber = 0;
                    for (String each : wordsArray){
                        if (columnNumber == 0) {
                            newCoorRow.city = each.toLowerCase();
                        } else if (columnNumber == 1){
                            newCoorRow.city_ascii = each.toLowerCase();
                        } else if (columnNumber == 2){
                            newCoorRow.state_id = each;
                        } else if (columnNumber == 3){
                            newCoorRow.state_name = each;
                        } else if (columnNumber == 4){
                            newCoorRow.county_name = each;
                        } else if (columnNumber == 5){
                            newCoorRow.county_fips = convertStringToInt(each);
                        } else if (columnNumber == 6){
                            newCoorRow.zip = convertStringToInt(each);
                        } else if (columnNumber == 7){
                            newCoorRow.lat = convertStringToDouble(each);
                        } else if (columnNumber == 8){
                            newCoorRow.lng = convertStringToDouble(each);
                        } else if (columnNumber == 9){
                            newCoorRow.population = convertStringToInt(each);
                        } else if (columnNumber == 10){
                            newCoorRow.source = each;
                        } else {
                            newCoorRow.id = convertStringToInt(each);
                        }
                        columnNumber++;
                    }
                    if (!coordinateData.containsKey(newCoorRow.state_id)) {
                        coordinateData.put(newCoorRow.state_id, new Hashtable<>());
                    }
                    if (!coordinateData.get(newCoorRow.state_id).containsKey(newCoorRow.city)) {
                        coordinateData.get(newCoorRow.state_id).put(newCoorRow.city, newCoorRow );
                    }
                }
            }
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return coordinateData;
    }
    public Hashtable<String, Hashtable<String, ArrayList<DataRow>>> readData(String filePath, int recordsToProcess)
    {
        Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data = new Hashtable<>();
        try{
            BufferedReader buf = new BufferedReader(new FileReader(filePath));
            String lineJustFetched = null;
            String[] wordsArray;
            int lineProcessed = 0;
            while(lineProcessed < recordsToProcess){
                lineJustFetched = buf.readLine();
                if(lineJustFetched == null){
                    break;
                }else{
                    lineProcessed++;
                    DataRow newRow = new DataRow();
                    wordsArray = lineJustFetched.split("\t");
                    int columnNumber = 0;
                    for(String each : wordsArray){
                        if (columnNumber == 0) {
                            newRow.npi = each;
                        } else if (columnNumber == 1) {
                            newRow.firstName = each;
                        } else if (columnNumber == 2) {
                            newRow.lastName = each;
                        } else if (columnNumber == 3) {
                            newRow.city = each.toLowerCase();
                        } else if (columnNumber == 4) {
                            newRow.state = each;
                        } else if (columnNumber == 5) {
                            newRow.specialty = each;
                        } else if (columnNumber == 6) {
                            newRow.descriptionFlag = each;
                        } else if (columnNumber == 7) {
                            newRow.drugName = each;
                        } else if (columnNumber == 8) {
                            newRow.genericName = each;
                        } else if (columnNumber == 9) {
                            newRow.beneCount = convertStringToInt(each);
                        } else if (columnNumber == 10) {
                            newRow.totalClaimCount = convertStringToInt(each);
                        } else if (columnNumber == 11) {
                            newRow.total30DayFillCount = convertStringToInt(each);
                        } else if (columnNumber == 12) {
                            newRow.totalDaySupply = convertStringToInt(each);
                        } else if (columnNumber == 13) {
                            newRow.totalDrugCost = convertStringToDouble(each);
                        } else if (columnNumber == 14) {
                            newRow.beneCountGe65 = convertStringToInt(each);
                        } else if (columnNumber == 15) {
                            newRow.beneCountGe65SuppressFlag = convertStringToInt(each);
                        } else if (columnNumber == 16) {
                            newRow.totalClaimCountGe65 = convertStringToInt(each);
                        } else if (columnNumber == 17) {
                            newRow.ge65SuppressFlag = convertStringToInt(each);
                        } else if (columnNumber == 18) {
                            newRow.total30DayFillCountGe65 = convertStringToInt(each);
                        } else if (columnNumber == 19) {
                            newRow.totalDaySupplyGe65 = convertStringToInt(each);
                        } else {
                            newRow.totalDrugCostGe65 = convertStringToDouble(each);
                        }
                        columnNumber++;
                    }
                    if (!data.containsKey(newRow.state)) {
                        data.put(newRow.state, new Hashtable<>());
                    }
                    if (!data.get(newRow.state).containsKey(newRow.city)) {
                        data.get(newRow.state).put(newRow.city, new ArrayList<>());
                    }
                    data.get(newRow.state).get(newRow.city).add(newRow);
                }
            }
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
    public Hashtable<String, Integer> densitySpecialty(Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data, String state, String city)
    {
        Hashtable<String, Integer> specialtyTable = new Hashtable<>();
        for (DataRow dataRow: data.get(state).get(city)) {
            if (!specialtyTable.containsKey(dataRow.specialty)) {
                specialtyTable.put(dataRow.specialty, 0);
            }
            specialtyTable.put(dataRow.specialty, specialtyTable.get(dataRow.specialty) + 1);
        }
        return specialtyTable;
    }
    public ArrayList getDensityMatrix(Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data, Hashtable<String, Hashtable<String, Coordinate>> geodata)
    {
        String firstName = "";
        String lastName = "";
        //Double[][] geoArr = new Double[matrixRow][2];
        ArrayList geoArr = new ArrayList();
        for (String state : data.keySet()){
            for (String city : data.get(state).keySet()){
                for (DataRow dataRow : data.get(state).get(city)){
                    if(!firstName.equals(dataRow.firstName) && !lastName.equals(dataRow.lastName)){
                        Double[] thisRow = new Double[2];
                        try {
                            Coordinate thisCoordinate = geodata.get(state).get(city);
                            thisRow[0] = thisCoordinate.lat;
                            thisRow[1] = thisCoordinate.lng;
                            geoArr.add(thisRow);
                        } catch(Exception e) {
                            System.err.println("City [" + city + "] is not found in Geo Data!");
                        }
                        firstName = dataRow.firstName;
                        lastName = dataRow.lastName;
                    }
                }
            }
        }
        return geoArr;
    }
//    public Double[][] specialtyMatrix(Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data, Hashtable<String, Hashtable<String, Coordinate>> geodata)
//    {
//        String firstName = "";
//        String lastName = "";
//        int matrixRow = 0;
//        Double[][] specialtyArr = new Double[matrixRow][3];
//        for (String state : data.keySet()){
//            for (String city : data.get(state).keySet()){
//                for (DataRow dataRow : data.get(state).get(city)){
//                    if(!firstName.equals(data.get(state).get(city).dataRow.firstName) && !lastName.equals(data.get(state).get(city).dataRow.lastName)){
//                        specialtyArr[matrixRow][0] = geodata.get(state).get(city).lat;
//                        specialtyArr[matrixRow][1] = geodata.get(state).get(city).lag;
//                        specialtyArr[matrixRow][2] = data.get(state).get(city).dataRow.specialty;
//                        matrixRow++;
//                        firstName = data.get(state).get(city).dataRow.firstName;
//                        lastName = data.get(state).get(city).dataRow.lastName;
//                    }
//                }
//            }
//        }
//        return specialtyArr;
//    }
    public static void main(String...args)
    {

        ManipulateData manipulateData = new ManipulateData();
        Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data = manipulateData.readData("data-src/drug_15_sample.txt", 20000);
//        for (String state : data.keySet()){
//            for (String city : data.get(state).keySet()){
//                Hashtable<String, Integer> specialtyTable = manipulateData.densitySpecialty(data,state,city);
//                for (String key : specialtyTable.keySet()) {
//                    System.out.print(state);
//                    System.out.print(" , ");
//                    System.out.print(city);
//                    System.out.print(" , ");
//                    System.out.print("Specialty = " + key + ": " + specialtyTable.get(key));
//                    System.out.println("");
//                }
//             }
//        }
        Hashtable<String, Hashtable<String, Coordinate>> dataCoor = manipulateData.readCoorData("data-src/uscitiesv1.3.csv");
        ArrayList<Double []> densityMatrix = manipulateData.getDensityMatrix(data, dataCoor);

        for (Double[] row : densityMatrix) {
            System.out.println(row[0] + " | " + row[1]);
        }
    }
}
