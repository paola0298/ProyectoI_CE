package Structures;

public class ExpertRequests {
    private LinkedList<String[]> requests;

    public ExpertRequests() {
        this.requests = new LinkedList<>();
    }

    public void addRequest(String[] request) {
        this.requests.addLast(request);
    }

    public void addRequest(String matchId, String expertPhone, String wordToCheck) {
        String[] req = new String[] {matchId, expertPhone, wordToCheck, ""};
        this.requests.addLast(req);
    }

    public String[] getRequest(int index) {
        return this.requests.get(index);
    }

    public void removeRequest(String[] request) {
        this.requests.remove(request);
    }

    public void updateRequest(String[] oldRequest, String[] newRequest) {
        this.requests.remove(oldRequest);
        this.requests.addLast(newRequest);
    }

    public LinkedList<String[]> getRequests() {
        return requests;
    }

    public void setRequests(LinkedList<String[]> requests) {
        this.requests = requests;
    }

    public int getSize() {
        return this.requests.getSize();
    }
}
