import java.util.Collection;

public class Result {
    private Collection<Customer> customers;

    public Result(Collection<Customer> customers) {
        this.customers = customers;
    }

    public Collection<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Collection<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "Result{" +
                "customers=" + customers +
                '}';
    }
}