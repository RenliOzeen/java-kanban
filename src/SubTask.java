public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, String details) {
        super(name, details);

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
