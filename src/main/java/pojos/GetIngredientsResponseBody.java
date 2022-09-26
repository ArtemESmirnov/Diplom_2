package pojos;

public class GetIngredientsResponseBody {
    private boolean success;
    private IngredientData[] data;

    public GetIngredientsResponseBody() {
    }

    public GetIngredientsResponseBody(boolean success, IngredientData[] data) {
        this.success = success;
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public IngredientData[] getData() {
        return data;
    }

    public void setData(IngredientData[] data) {
        this.data = data;
    }
}
