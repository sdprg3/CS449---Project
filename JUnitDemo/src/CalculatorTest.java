import org.junit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    Calculator calculator = new Calculator();

    @Test
    public void testAdd() {
        // Test case 1: Testing addition
        int result = calculator.add(20, 30);
        System.out.println("Testing addition of 20 and 30. Expected: 50, Got: " + result);
        assertEquals(50, result, "Addition test failed. Expected 50 but got " + result);
    }


    @Test
    public void testSubtract() {
        // Test case 3: Testing multiplication
        int result = calculator.multiply(5, 6);
        System.out.println("Testing multiplication of 5 and 6. Expected: 30, Got: " + result);
        assertEquals(30, result, "Multiplication test failed. Expected 30 but got " + result);
    }




}

