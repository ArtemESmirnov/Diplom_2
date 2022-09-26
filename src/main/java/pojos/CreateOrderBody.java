package pojos;

public class CreateOrderBody {
    private String[] ingredients;

    public CreateOrderBody(){}

    public CreateOrderBody(String[] ingredients){
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
