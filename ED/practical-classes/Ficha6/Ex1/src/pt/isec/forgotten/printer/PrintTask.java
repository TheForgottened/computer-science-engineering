package pt.isec.forgotten.printer;

public record PrintTask(
        String fileName,
        int startingPage,
        int endingPage
) {
    public int totalPages() { return endingPage - startingPage; }
}
