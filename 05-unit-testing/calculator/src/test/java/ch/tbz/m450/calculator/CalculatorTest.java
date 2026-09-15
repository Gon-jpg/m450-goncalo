package ch.tbz.m450.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {

  private final Calculator calculator = new Calculator();

  // --- add ---

  @Test
  void add_TwoPositiveNumbers_ReturnsSum() {
    assertEquals(8.0, calculator.add(5.0, 3.0));
  }

  @Test
  void add_PositiveAndNegative_ReturnsCorrectSum() {
    assertEquals(2.0, calculator.add(5.0, -3.0));
  }

  @Test
  void add_TwoNegativeNumbers_ReturnsNegativeSum() {
    assertEquals(-8.0, calculator.add(-5.0, -3.0));
  }

  // --- subtract ---

  @Test
  void subtract_PositiveNumbers_ReturnsDifference() {
    assertEquals(2.0, calculator.subtract(5.0, 3.0));
  }

  @Test
  void subtract_ResultIsNegative_ReturnsNegativeDifference() {
    assertEquals(-2.0, calculator.subtract(3.0, 5.0));
  }

  @Test
  void subtract_SameNumbers_ReturnsZero() {
    assertEquals(0.0, calculator.subtract(4.0, 4.0));
  }

  // --- multiply ---

  @Test
  void multiply_TwoPositiveNumbers_ReturnsProduct() {
    assertEquals(15.0, calculator.multiply(5.0, 3.0));
  }

  @Test
  void multiply_ByZero_ReturnsZero() {
    assertEquals(0.0, calculator.multiply(5.0, 0.0));
  }

  @Test
  void multiply_TwoNegativeNumbers_ReturnsPositiveProduct() {
    assertEquals(15.0, calculator.multiply(-5.0, -3.0));
  }

  // --- divide ---

  @Test
  void divide_PositiveNumbers_ReturnsQuotient() {
    assertEquals(2.5, calculator.divide(5.0, 2.0));
  }

  @Test
  void divide_NegativeDividend_ReturnsNegativeQuotient() {
    assertEquals(-2.5, calculator.divide(-5.0, 2.0));
  }

  @Test
  void divide_ByZero_ThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> calculator.divide(5.0, 0.0));
  }
}
