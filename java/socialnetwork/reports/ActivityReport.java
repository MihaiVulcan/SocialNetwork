package socialnetwork.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageManagementService;
import socialnetwork.service.UserService;

import java.io.FileOutputStream;
import java.time.LocalDate;

public class ActivityReport {
    MessageManagementService messageService;
    FriendshipService friendshipService;
    UserService userService;

    public ActivityReport(MessageManagementService messageService, FriendshipService friendshipService, UserService userService) {
        this.messageService = messageService;
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    public void save(String user, String Path, LocalDate from, LocalDate to){
        try{
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(Path));
            addData(document, user, from , to);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addData(Document document, String user, LocalDate from, LocalDate to){

        try {
            document.open();
            document.add(new Paragraph("Raport creat in data " + LocalDate.now().toString()));
            document.add(new Paragraph("Interval de timp " + from.toString() + " - " + to.toString()));
            document.add(new Paragraph("\n\n"));
            Paragraph newf = new Paragraph("New Friends");
            newf.setAlignment(10);
            document.add(newf);
            List friends = new List();
            friendshipService.getFriends(user).forEach(e->{
                if(e.getLocalDateTime().toLocalDate().isAfter(from) && e.getLocalDateTime().toLocalDate().isBefore(to)){
                    friends.add(new ListItem(e.getName() + " " + e.getLocalDateTime().toLocalDate().toString()));
                }
            });
            document.add(friends);

            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Messages"));
            List messages = new List();
            messageService.getMessagesReceivedByUser(user).forEach(e->{
                if(e.getTimestamp().toLocalDate().isAfter(from) && e.getTimestamp().toLocalDate().isBefore(to))
                    messages.add(new ListItem(e.getTimestamp().toLocalDate().toString() + " " +
                            e.getTimestamp().toLocalTime().toString() + " " + e.getName() + ": " + e.getMessage()));
            });
            document.add(messages);
            document.close();
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }
}
