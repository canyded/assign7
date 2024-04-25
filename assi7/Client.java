import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Document {
    private String id;
    private String content;

    public Document(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

interface DocumentManagementSystem {
    void uploadDocument(String username, Document document);
    Document downloadDocument(String username, String documentId);
    void editDocument(String username, String documentId, Document newVersion);
}

class RealDocumentManagementSystem implements DocumentManagementSystem {
    private Map<String, Document> documents;

    public RealDocumentManagementSystem() {
        this.documents = new HashMap<>();
    }

    @Override
    public void uploadDocument(String username, Document document) {
        if (isAuthorized(username)) {
            documents.put(document.getId(), document);
            System.out.println(username + " uploaded document: " + document.getId());
        } else {
            System.out.println("Unauthorized access attempt by " + username);
        }
    }

    @Override
    public Document downloadDocument(String username, String documentId) {
        if (isAuthorized(username)) {
            Document document = documents.get(documentId);
            if (document != null) {
                System.out.println(username + " downloaded document: " + document.getId());
                return document;
            } else {
                System.out.println("Document " + documentId + " not found");
            }
        } else {
            System.out.println("Unauthorized access attempt by " + username);
        }
        return null;
    }

    @Override
    public void editDocument(String username, String documentId, Document newVersion) {
        if (isAuthorized(username)) {
            if (documents.containsKey(documentId)) {
                documents.put(documentId, newVersion);
                System.out.println(username + " edited document: " + documentId);
            } else {
                System.out.println("Document " + documentId + " not found");
            }
        } else {
            System.out.println("Unauthorized access attempt by " + username);
        }
    }

    private boolean isAuthorized(String username) {
        return true;
    }
}

class DocumentManagementProxy implements DocumentManagementSystem {
    private RealDocumentManagementSystem realSystem;

    public DocumentManagementProxy() {
        this.realSystem = new RealDocumentManagementSystem();
    }

    @Override
    public void uploadDocument(String username, Document document) {
        System.out.println("User " + username + " is attempting to upload document...");
        realSystem.uploadDocument(username, document);
    }

    @Override
    public Document downloadDocument(String username, String documentId) {
        System.out.println("User " + username + " is attempting to download document...");
        return realSystem.downloadDocument(username, documentId);
    }

    @Override
    public void editDocument(String username, String documentId, Document newVersion) {
        System.out.println("User " + username + " is attempting to edit document...");
        realSystem.editDocument(username, documentId, newVersion);
    }
}

public class Client {
    private static final String[] USERNAMES = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};

    public static void main(String[] args) {
        DocumentManagementSystem system = new DocumentManagementProxy();

        Document document = new Document("doc1", "Sample content");
        String uploader = getRandomUsername();
        system.uploadDocument(uploader, document);

        String downloader = getRandomUsername();
        Document downloadedDocument = system.downloadDocument(downloader, document.getId());
        if (downloadedDocument != null) {
            System.out.println(downloader + " downloaded Document: " + downloadedDocument.getContent());
        }

        Document newVersion = new Document(document.getId(), "Updated content");
        String editor = getRandomUsername();
        system.editDocument(editor, document.getId(), newVersion);
    }

    private static String getRandomUsername() {
        Random random = new Random();
        return USERNAMES[random.nextInt(USERNAMES.length)];
    }
}
