import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

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
    public ArrayList getDensityMatrix(Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data, Hashtable<String, Hashtable<String, Coordinate>> geodata)
    {

        //Double[][] geoArr = new Double[matrixRow][2];
        ArrayList<Double[]> geoArr = new ArrayList<>();
        for (String state : data.keySet()){
            for (String city : data.get(state).keySet()) {
                HashSet<String> names = new HashSet<>();
                for (DataRow dataRow : data.get(state).get(city)) {
                    if (!names.contains(dataRow.firstName + dataRow.lastName)) {
                        Double[] thisRow = new Double[2];
                        try {
                            Coordinate thisCoordinate = geodata.get(state).get(city);
                            thisRow[0] = thisCoordinate.lat;
                            thisRow[1] = thisCoordinate.lng;
                            geoArr.add(thisRow);
                        } catch (Exception e) {
                            System.err.println("City [" + city + "] is not found in Geo Data!");
                        }
                        names.add(dataRow.firstName + dataRow.lastName);
                    }
                }
            }
        }
        return geoArr;
    }
    public void kmeanDensity(ArrayList<Double []> matrix){
        Dataset dataset = new DefaultDataset();
        for (Double[] row : matrix) {
            double[] doubleRow = {row[0], row[1]};
            Instance thisInstance = new DenseInstance(doubleRow);
            dataset.add(thisInstance);
        }

        Clusterer km = new KMeans();
        Dataset[] clusters = km.cluster(dataset);
        for (Dataset cluster : clusters) {
            double sumX = 0, sumY = 0;
            for (Instance point : cluster) {
                sumX += point.get(0);
                sumY += point.get(1);
            }
            double meanX = sumX / cluster.size(), meanY = sumY / cluster.size();
            System.out.println("cluster Size = " + cluster.size() + ", Mean lat = " + meanX + ", Mean lag = " + meanY);
        }
    }
    public Hashtable<String,ArrayList<Double[]>> getspecialtyHashtable(Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data, Hashtable<String, Hashtable<String, Coordinate>> geoData){
        Hashtable<String,ArrayList<Double[]>> specialtyHashtable = new Hashtable<>();
        for (String state : data.keySet()){
            for (String city : data.get(state).keySet()){
                HashSet<String> names = new HashSet<>();
                for (DataRow newRow : data.get(state).get(city)){
                    if(!names.contains(newRow.firstName + newRow.lastName)){
                        if (!specialtyHashtable.containsKey(newRow.specialty) && geoData.get(state).containsKey(city)) {
                            specialtyHashtable.put(newRow.specialty, new ArrayList<>());
                        }
                        Double[] thisRow = new Double[2];
                        try {
                            Coordinate thisCoordinate = geoData.get(state).get(city);
                            thisRow[0] = thisCoordinate.lat;
                            thisRow[1] = thisCoordinate.lng;
                            specialtyHashtable.get(newRow.specialty).add(thisRow);
                        } catch(Exception e) {
                            System.err.println("City [" + city + "] is not found in Geo Data!");
                        }
                        names.add(newRow.firstName + newRow.lastName);
                    }
                }
            }
        }
        return specialtyHashtable;
    }
    public void kmeanSpecialty(Hashtable<String, ArrayList<Double []>> specialtyHashtable){
        for (String specialty : specialtyHashtable.keySet()){
            System.out.println("Specialty: "+ specialty);
            ArrayList<Double []> matrix = specialtyHashtable.get(specialty);
            Dataset dataset = new DefaultDataset();
            if (matrix.size() < 4) {
                System.out.println("Cluster Size = " + matrix.size() );
                for (Double[] i : matrix) {
                    System.out.println("lat =  " + i[0] + ", " + "Lag =  " + i[1]);
                }
                continue;
            }
            for (Double[] row : matrix) {
                double[] doubleRow = {row[0], row[1]};
                Instance thisInstance = new DenseInstance(doubleRow);
                dataset.add(thisInstance);
            }
            Clusterer km = new KMeans();
            Dataset[] clusters = km.cluster(dataset);
            for (Dataset cluster : clusters) {
                double sumX = 0, sumY = 0;
                for (Instance point : cluster) {
                    sumX += point.get(0);
                    sumY += point.get(1);
                }
                double meanX = sumX / cluster.size(), meanY = sumY / cluster.size();
                System.out.println("Cluster Size = " + cluster.size() + ", "+ " Mean lat =  " + meanX + ", "+ "Mean Lag =  " + meanY);
            }
        }
    }
    public static void main(String...args)
    {
        ManipulateData manipulateData = new ManipulateData();
        Hashtable<String, Hashtable<String, ArrayList<DataRow>>> data = manipulateData.readData("data-src/drug_15_sample.txt", 20000);
        Hashtable<String, Hashtable<String, Coordinate>> dataCoor = manipulateData.readCoorData("data-src/uscitiesv1.3.csv");
        ArrayList<Double []> densityMatrix = manipulateData.getDensityMatrix(data, dataCoor);
        System.out.println("**********************************************************************************************");
        System.out.println("Providers Density Clustering");
        manipulateData.kmeanDensity(densityMatrix);
        System.out.println("**********************************************************************************************");
        Hashtable<String,ArrayList<Double[]>> specialtyHash= manipulateData.getspecialtyHashtable(data, dataCoor);
        System.out.println("Providers Specialty Clustering");
        manipulateData.kmeanSpecialty(specialtyHash);
        System.out.println("Done!!!!!");
    }
}
