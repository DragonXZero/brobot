package brobot;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {
    private final List<StringBuilder> responseBldrs;
    private StringBuilder currentResponseBldr;
    private final List<String> images;

    public ResponseObject() {
        this.responseBldrs = new ArrayList<>();
        this.currentResponseBldr = new StringBuilder();
        images = new ArrayList<>();
    }

    public StringBuilder getResponseBldr() {
        return this.currentResponseBldr;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void addMessage(final String message) {
        if (currentResponseBldr.length() + message.length() >= 2000) {
            responseBldrs.add(new StringBuilder(currentResponseBldr.toString()));
            currentResponseBldr = new StringBuilder();
        }

        currentResponseBldr.append(message);
    }

    public List<StringBuilder> finalizeAndGetBldrs() {
        responseBldrs.add(currentResponseBldr);
        return responseBldrs;
    }

    public void addImage(final String image) {
        this.images.add(image);
    }
}
