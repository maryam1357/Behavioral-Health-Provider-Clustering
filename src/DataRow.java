
public class DataRow
{
    String npi;
    String lastName;
    String firstName;
    String city;
    String state;
    String specialty;
    String descriptionFlag;
    String drugName;
    String genericName;
    int beneCount;
    int totalClaimCount;
    int total30DayFillCount;
    int totalDaySupply;
    double totalDrugCost;
    int beneCountGe65;
    int beneCountGe65SuppressFlag;
    int totalClaimCountGe65;
    int ge65SuppressFlag;
    int total30DayFillCountGe65;
    int totalDaySupplyGe65;
    double totalDrugCostGe65;

    @Override
    public String toString() {
        return new String("DataRow : "
                + "npi = " + npi + '\n'
                + "lastName = " + lastName + '\n'
                + "firstName = " + firstName + '\n'
                + "city = " + city + '\n'
                + "state = " + state + '\n'
                + "specialty = " + specialty + '\n'
                + "descriptionFlag = " + descriptionFlag + '\n'
                + "drugName = " + drugName + '\n'
                + "genericName = " + genericName + '\n'
                + "beneCount = " + beneCount + '\n'
                + "totalClaimCount = " + totalClaimCount + '\n'
                + "total30DayFillCount = " + total30DayFillCount + '\n'
                + "totalDaySupply = " + totalDaySupply + '\n'
                + "totalDrugCost = " + totalDrugCost + '\n'
                + "beneCountGe65 = " + beneCountGe65 + '\n'
                + "beneCountGe65SuppressFlag = " + beneCountGe65SuppressFlag + '\n'
                + "totalClaimCountGe65 = " + totalClaimCountGe65 + '\n'
                + "ge65SuppressFlag = " + ge65SuppressFlag + '\n'
                + "total30DayFillCountGe65 = " + total30DayFillCountGe65 + '\n'
                + "totalDaySupplyGe65 = " + totalDaySupplyGe65 + '\n'
                + "totalDrugCostGe65 = " + totalDrugCostGe65 + '\n');
    }
}