import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your actual API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String urlString = API_URL + API_KEY + "/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return extractExchangeRate(response.toString(), targetCurrency);
        } catch (Exception e) {
            System.out.println("Error fetching exchange rate. Please check your API key and internet connection.");
            return -1;
        }
    }

    public static double extractExchangeRate(String json, String targetCurrency) {
        String searchKey = "\"" + targetCurrency + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            System.out.println("Currency not found in the API response.");
            return -1;
        }

        startIndex += searchKey.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }

        try {
            return Double.parseDouble(json.substring(startIndex, endIndex));
        } catch (Exception e) {
            System.out.println("Error parsing exchange rate.");
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter base currency (e.g., USD, EUR, INR): ");
        String baseCurrency = scanner.next().toUpperCase();

        System.out.print("Enter target currency (e.g., USD, EUR, INR): ");
        String targetCurrency = scanner.next().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate == -1) {
            System.out.println("Currency conversion failed.");
        } else {
            double convertedAmount = amount * exchangeRate;
            System.out.println(amount + " " + baseCurrency + " = " + String.format("%.2f", convertedAmount) + " " + targetCurrency);
        }

        scanner.close();
    }
}
