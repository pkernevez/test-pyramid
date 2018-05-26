package ch.octo.blog.transport.journeybooking;

class JourneyNotFoundException extends RuntimeException {

    static final String MSG_JOURNEY_NOT_FOUND = "Journey %d not found";

    JourneyNotFoundException(String s) {
        super(s);
    }
}
