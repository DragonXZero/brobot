package brobot;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {
    private final StringBuilder responseBldr;
    // TODO - Might want to create a wrapper around this.
    private final List<String> images;

    public ResponseObject() {
        this.responseBldr = new StringBuilder();
        images = new ArrayList<>();
    }

    public StringBuilder getResponseBldr() {
        return this.responseBldr;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void addMessage(final String message) {
        responseBldr.append(message);
    }

    public void addImage(final String image) {
        this.images.add(image);
    }
}
