package ee.ut.esi.group4.rentit.common.application.exception;

public class PlantNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlantNotFoundException(Long id) {
        super(String.format("Plant not found! (Plant id: %d)", id));
    }
}
