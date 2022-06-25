public class TestSql {
    static String basic = "SELECT * FROM `Coupons_Project`.";

    public static void main(String[] args) {
        String[] queryArgs = {"id","email"};
        System.out.println(generateQuery("copons", queryArgs));
    }

    private static String generateQuery(String tableName, String[] args) {
        System.out.println(args.length);
        String query = basic + "'" + tableName + "'";
        if (args.length > 0) {
            query += " WHERE ";
            int argsCount = 0;
            for (String arg : args) {
                argsCount++;
                query += "('" + arg + "' = ?) ";
                if(argsCount < args.length)
                    query += " and ";
            }
        }
        query+=";";
        return query;

    }


}
