using System;

class Program
{
    static void Main()
    {
        var calculator = new PriceCalculator();

        RunTest("No extras, no discount", () =>
            AssertEqual(calculator.CalculatePrice(100, 20, 50, 0, 0), 170.0));

        RunTest("Extras below threshold (2) -> no addon discount", () =>
            AssertEqual(calculator.CalculatePrice(100, 0, 100, 2, 0), 200.0));

        RunTest("Extras at threshold (3) -> 10% addon discount", () =>
            AssertEqual(calculator.CalculatePrice(100, 0, 100, 3, 0), 190.0));

        RunTest("Extras above threshold (5) -> still 10% (unreachable 15% branch)", () =>
            AssertEqual(calculator.CalculatePrice(100, 0, 100, 5, 0), 190.0));

        RunTest("Discount overrides addon discount when larger", () =>
            AssertEqual(calculator.CalculatePrice(100, 0, 100, 3, 20), 160.0));

        RunTest("Discount smaller than addon discount -> addon discount wins for extras", () =>
            AssertEqual(calculator.CalculatePrice(100, 0, 100, 3, 5), 185.0));

        RunTest("Special price adds flat with no discount applied to it", () =>
            AssertEqual(calculator.CalculatePrice(0, 75, 0, 0, 50), 75.0));

        RunTest("Zero extras, zero base, only special price", () =>
            AssertEqual(calculator.CalculatePrice(0, 42, 0, 0, 0), 42.0));

        RunTest("Negative discount (edge case, not validated by method)", () =>
            AssertEqual(calculator.CalculatePrice(100, 0, 0, 0, -10), 110.0));

        Console.WriteLine("\nAll tests completed.");
    }

    static void RunTest(string name, Action test)
    {
        try
        {
            test();
            Console.WriteLine($"[PASS] {name}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[FAIL] {name} — {ex.Message}");
        }
    }

    static void AssertEqual(double actual, double expected, double tolerance = 0.0001)
    {
        if (Math.Abs(actual - expected) > tolerance)
            throw new Exception($"Expected {expected}, got {actual}");
    }
}
