class SupportRequest {
    private int id;
    private String description;
    private int priority;

    public SupportRequest(int id, String description, int priority) {
        this.id = id;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}

interface Handler {
    void handleRequest(SupportRequest request);
}

class HardwareHandler implements Handler {
    private Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handleRequest(SupportRequest request) {
        if (request.getPriority() <= 3) {
            System.out.println("Hardware team is handling request: " + request.getId());
        } else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}

class SoftwareHandler implements Handler {
    private Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handleRequest(SupportRequest request) {
        if (request.getPriority() > 3 && request.getPriority() <= 6) {
            System.out.println("Software team is handling request: " + request.getId());
        } else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}

class NetworkHandler implements Handler {
    public void handleRequest(SupportRequest request) {
        if (request.getPriority() > 6) {
            System.out.println("Network team is handling request: " + request.getId());
        }
    }
}

public class HelpDeskSystemTest {
    public static void main(String[] args) {
        Handler hardwareHandler = new HardwareHandler();
        Handler softwareHandler = new SoftwareHandler();
        Handler networkHandler = new NetworkHandler();

        ((HardwareHandler) hardwareHandler).setNextHandler(softwareHandler);
        ((SoftwareHandler) softwareHandler).setNextHandler(networkHandler);

        SupportRequest request1 = new SupportRequest(1, "Hardware issue", 2);
        SupportRequest request2 = new SupportRequest(2, "Software issue", 5);
        SupportRequest request3 = new SupportRequest(3, "Network issue", 8);

        hardwareHandler.handleRequest(request1);
        hardwareHandler.handleRequest(request2);
        hardwareHandler.handleRequest(request3);
    }
}
