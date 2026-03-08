package fr.bookswap.common.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Long id) {
        super("Element non trouvée avec l'id : " + id);
    }

    public NotFoundException(String message) {
        super(message);
    }
}