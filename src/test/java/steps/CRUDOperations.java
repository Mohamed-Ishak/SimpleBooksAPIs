package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import utils.ConfigManager;
import utils.GenerateRandomData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class CRUDOperations {

    private final String ACCESS_TOKEN_FILE = ConfigManager.getProperty("accessTokenFile");
    private final String ORDER_ID_FILE = ConfigManager.getProperty("orderIdFile");

    private void writeToFile(String filePath, String data) throws IOException {
        Files.write(Paths.get(filePath), data.getBytes());
    }

    private String readFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Given("I registered a new api client")
    public void registerAPIClient() throws IOException {
        String payloadPath = "src/test/resources/testData/RegisterAPIClientPayload.json";
        // Read the JSON payload
        String payload = new String(Files.readAllBytes(Paths.get(payloadPath)));

        String uniqueEmailAddress = GenerateRandomData.generateUniqueEmail();
        String requiredPayload  =payload.replace("valentin@example.com",uniqueEmailAddress);


        // Execute API request
        String response = given()
                .header("Content-Type", "application/json")
                .body(requiredPayload)
                .when()
                .post("/api-clients")
                .then()
                .extract().body().asString();

        JsonPath js = new JsonPath(response);
        String accessToken  = js.getString("accessToken");
//      System.out.println("accessToken = " + accessToken);
        writeToFile(ACCESS_TOKEN_FILE, accessToken);
    }

    @When("I Create a new order")
    public void createNewOrder() throws IOException {
        String payloadPath = "src/test/resources/testData/PostNewOrderPayload.json";
        String NewOrderPayload = new String(Files.readAllBytes(Paths.get(payloadPath)));

        String accessToken = readFromFile(ACCESS_TOKEN_FILE);
        String responseNewOrder = given()
                .headers(
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + accessToken)
                .body(NewOrderPayload)
                .when()
                .post("/orders")
                .then().assertThat().statusCode(201)
                .log().body().extract().response().asString();

        // Save token to orderId.txt
        String order_Id = new JsonPath(responseNewOrder).getString("orderId");
        writeToFile(ORDER_ID_FILE,order_Id);

        boolean createdStatus = Boolean.parseBoolean(new JsonPath(responseNewOrder).getString("created"));
        Assert.assertTrue(createdStatus);
    }

    @And("I get all books")
    public void getAllSampleBooks(){
        String response = given()
                .header("Content-Type","application/json")
                .when()
                .get("/books")
                .then().assertThat().statusCode(200)
                .log().body().extract().response().asString();

        System.out.println("response = " + response);
    }

    @When("I retrieve the order by Id")
    public void getTheOrderByID() throws IOException {
       String accessToken = readFromFile(ACCESS_TOKEN_FILE);
       String orderId = readFromFile(ORDER_ID_FILE);
        String response = given()
                .headers(        "Content-Type", "application/json",
                        "Authorization", "Bearer " + accessToken)
                .when()
                .get("/orders/"+ orderId)
                .then().assertThat().statusCode(200)
                .log().body().extract().response().asString();
        System.out.println("response = " + response);

    }

    @When("I update the order")
    public void updateOrder() throws IOException {
        String accessToken = readFromFile(ACCESS_TOKEN_FILE);
        String orderId = readFromFile(ORDER_ID_FILE);
        given()
                    .headers(     "Content-Type", "application/json",
                            "Authorization", "Bearer " + accessToken
                    )
                .when()
                    .patch("/orders/"+ orderId)
                .then().assertThat().statusCode(204)
                    .log().body().extract().response().asString();
    }

    @When("I delete the order")
    public void deleteOrder() throws IOException {
        String accessToken = readFromFile(ACCESS_TOKEN_FILE);
        String orderId = readFromFile(ORDER_ID_FILE);
        given()
                .headers(     "Content-Type", "application/json",
                        "Authorization", "Bearer " + accessToken
                )
                .when()
                .delete("/orders/"+ orderId)
                .then().assertThat().statusCode(204)
                .log().body().extract().response().asString();
    }

    @Then("Validate that order has been deleted")
    public void validateOrderIsDeleted() throws IOException {
        String accessToken = readFromFile(ACCESS_TOKEN_FILE);
        String orderId = readFromFile(ORDER_ID_FILE);
        String response = given()
                .headers(        "Content-Type", "application/json",
                        "Authorization", "Bearer " + accessToken)
                .when()
                .get("/orders/"+ orderId)
                .then().assertThat().statusCode(404)
                .log().body().extract().response().asString();
        JsonPath js = new JsonPath(response);
        String errorMessage = js.getString("error");
        Assert.assertEquals(errorMessage,"No order with id "+orderId+".");
    }
}
