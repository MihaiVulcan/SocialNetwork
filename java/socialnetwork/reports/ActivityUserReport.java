package socialnetwork.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageManagementService;
import socialnetwork.service.UserService;

import java.io.FileOutputStream;
import java.time.LocalDate;

public class ActivityUserReport {
    MessageManagementService messageService;
    FriendshipService friendshipService;
    UserService userService;

    public ActivityUserReport(MessageManagementService messageService, FriendshipService friendshipService, UserService userService) {
        this.messageService = messageService;
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    public void save(String user, String Path, LocalDate from, LocalDate to, String friend){
        try{
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(Path));
            addData(document, user, from , to, friend);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addData(Document document, String user, LocalDate from, LocalDate to, String friend){

        try {
            document.open();
            document.add(new Paragraph("Raport creat in data " + LocalDate.now().toString()));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Interval de timp " + from.toString() + " - " + to.toString()));
            document.add(new Paragraph("Mesaje primite de la " + friend));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Messages"));
            List messages = new List();
            messageService.getMessagesReceivedByUser(user).forEach(e->{
                if(e.getTimestamp().toLocalDate().isAfter(from) && e.getTimestamp().toLocalDate().isBefore(to) && friend.equals(e.getName()))
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
