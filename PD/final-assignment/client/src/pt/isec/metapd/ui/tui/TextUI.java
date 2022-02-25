package pt.isec.metapd.ui.tui;

import pt.isec.metapd.logic.MetaPdSm;
import pt.isec.metapd.logic.data.MetaPDData;
import pt.isec.metapd.logic.exceptions.InvalidTextUIOptionException;
import pt.isec.metapd.logic.files.FileUtility;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextUI {
    private final MetaPdSm metaPdSM;
    private Scanner sc;
    String chosenOptionString;
    String chosenOptionString2;
    String chosenOptionString3;

    public TextUI(MetaPdSm metaPdSM) {
        this.metaPdSM = metaPdSM;
    }

    public void run() throws IOException {
        while(!metaPdSM.getMustStop()){
            if(metaPdSM.isAwaitingLogin()){
                uiLoginPage();
            }else if(metaPdSM.isAwaitingSignUp()){
                uiSignUpPage();
            }else if(metaPdSM.isAwaitingCommand()) {
                uiCommandPage();
            }
        }
    }

    private void uiLoginPage() {
        System.out.println("################# METAPD #################");
        System.out.println("#### ------------ Login Page ------------");
        System.out.println("#### L - Login");
        System.out.println("#### S - Sign up");
        System.out.println("#### ------------------------------------");
        System.out.println("#### Q - Quit");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[LlSsQq]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString){
            case "L":
            case "l":
                System.out.print("Username:");
                sc = new Scanner(System.in);
                String username = sc.next();
                System.out.print("Password:");
                String password = sc.next();
                metaPdSM.login(username, password);
                break;
            case "S":
            case "s":
                metaPdSM.goBack();
                break;
            case "Q":
            case "q":
                metaPdSM.setMustStop(true);
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiSignUpPage() {
        System.out.println("################# METAPD #################");
        System.out.println("#### ------------ SignUp Page ------------");
        System.out.println("#### S - Sign up");
        System.out.println("#### L - Login");
        System.out.println("#### -------------------------------------");
        System.out.println("#### Q - Quit");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[LlSsQq]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString){
            case "S":
            case "s":
                System.out.print("Username:");
                sc = new Scanner(System.in);
                String username = sc.nextLine();
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Password:");
                String password = sc.nextLine();
                metaPdSM.signup(username, name, password);
                break;
            case "L":
            case "l":
                metaPdSM.goBack();
                break;
            case "Q":
            case "q":
                metaPdSM.setMustStop(true);
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiCommandPage() {
        System.out.println("################# METAPD #################");
        System.out.println("################# Hello, "+ metaPdSM.getUsername()+"["+metaPdSM.getName()+"]");
        System.out.println("########################################");
        System.out.println("#### ------------ MENU ------------");
        System.out.println("#### P - Profile");
        System.out.println("#### U - Users");
        System.out.println("#### G - Groups");
        System.out.println("#### ------------------------------");
        System.out.println("#### L - Logout");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[PpUuGgLl]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "P":
            case "p":
                uiProfile();
                break;
            case "U":
            case "u":
                uiUsers();
                break;
            case "G":
            case "g":
                uiGroups();
                break;
            case "L":
            case "l":
                metaPdSM.goBack();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiProfile(){
        System.out.println("########################################");
        System.out.println("##### ---------- PROFILE ----------");
        System.out.println("##### N - Edit User Name");
        System.out.println("##### P - Edit User Password");
        System.out.println("##### ------------------------------");
        System.out.println("##### B - Back");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[NnPpBb]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "N":
            case "n":
                System.out.print("New Name:");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if (metaPdSM.makeCommand(0, chosenOptionString)) {
                    System.out.println("The name was changed successfully!");
                } else {
                    System.out.println("Impossible to change the name!");
                }
                uiProfile();
                break;
            case "P":
            case "p":
                System.out.print("New Password:");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if (metaPdSM.makeCommand(1, chosenOptionString)) {
                    System.out.println("The password was changed successfully!");
                    metaPdSM.goBack();
                } else {
                    System.out.println("Impossible to change password!");
                }
                break;
            case "B":
            case "b":
                uiCommandPage();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiUsers() {
        System.out.println("########################################");
        System.out.println("##### ------------ USERS -----------");
        System.out.println("##### U - List Every User");
        System.out.println("##### A - List By Name And Username");
        System.out.println("##### R - Friend Requests");
        System.out.println("##### C - List Contacts List");
        System.out.println("##### D - Delete Contact from Contacts List");
        System.out.println("##### M - Send Simple Message to Contact");
        System.out.println("##### F - Send Message with File to Contact");
        System.out.println("##### L - List Messages From Contacts List");
        System.out.println("##### T - Delete Message");
        System.out.println("##### ------------------------------");
        System.out.println("##### B - Back");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[UuAaRrCcDdMmFfLlTtBb]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "U":
            case "u":
                if(metaPdSM.makeCommand(2)){
                    System.out.println(metaPdSM.getSystemUserList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiUsers();
                break;
            case "A":
            case "a":
                System.out.print("Name to search: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(2,chosenOptionString)){
                    System.out.println(metaPdSM.getSystemUserList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiUsers();
                break;
            case "R":
            case "r":
                uiFriendRequests();
                break;
            case "C":
            case "c":
                if(metaPdSM.makeCommand(3)){
                    System.out.println(metaPdSM.getContactList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiUsers();
                break;
            case "D":
            case "d":
                System.out.print("Username to delete: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(4,chosenOptionString)){
                    System.out.println(chosenOptionString+" was deleted from your Contact List!");
                }else{
                    System.out.println("Impossible to delete the contact");
                }
                uiUsers();
                break;
            case "M":
            case "m":
                System.out.print("Send to: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("Message: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                if(metaPdSM.makeCommand(5,chosenOptionString,chosenOptionString2)){
                    System.out.println("The message was sent!");
                }else{
                    System.out.println("Impossible to send the message");
                }
                uiUsers();
                break;
            case "F":
            case "f":
                System.out.print("Send to: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("Message: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                System.out.print("File: ");
                sc = new Scanner(System.in);
                chosenOptionString3 = sc.nextLine();
                if(chosenOptionString3 == null || chosenOptionString3.length() < 1){
                    System.out.println("Impossible to manage the file");
                    uiUsers();
                    break;
                }
                try {
                    File file = (File) FileUtility.loadFile(chosenOptionString3);
                    if(metaPdSM.makeCommand(5,chosenOptionString,chosenOptionString2,file)){
                        System.out.println("The message was sent!");
                    }else{
                        System.out.println("Impossible to send the message");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                uiUsers();
                break;
            case "L":
            case "l":
                if(metaPdSM.makeCommand(6)){
                    System.out.println(metaPdSM.getMessageContactList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiUsers();
                break;
            case "T":
            case "t":
                System.out.print("Delete Message with MessageID: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(8,chosenOptionString)){
                    System.out.println("Message deleted!");
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiUsers();
                break;
            case "B":
            case "b":
                uiCommandPage();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiFriendRequests() {
        System.out.println("########################################");
        System.out.println("##### --------- FRIEND REQUESTS --------");
        System.out.println("##### S - Send Friend Request");
        System.out.println("##### M - My Friend Request");
        System.out.println("##### A - Answer Friend Requests");
        System.out.println("##### ------------------------------");
        System.out.println("##### B - Back");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[SsMmAaBb]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "S":
            case "s":
                System.out.print("Username: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(10,chosenOptionString)){
                    System.out.println("The Friend Request was sent!");
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiFriendRequests();
                break;
            case "M":
            case "m":
                if(metaPdSM.makeCommand(11)){
                    System.out.println("### Friend Requests ###");
                    System.out.println(metaPdSM.getFriendshipRequestList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiFriendRequests();
                break;
            case "A":
            case "a":
                System.out.print("Username: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.println("A - Accept");
                System.out.println("D - Decline");
                System.out.println("W - Wait");
                sc = new Scanner(System.in);
                while(!sc.hasNext("[AaDdWw]")){
                    sc.next();
                }
                chosenOptionString2 = sc.nextLine();
                switch (chosenOptionString2){
                    case "A":
                    case "a":
                        if(metaPdSM.makeCommand(12,chosenOptionString)){
                            System.out.println(chosenOptionString+" Friend Request Accepted!");
                        }else{
                            System.out.println("Impossible to accept the friend request by "+chosenOptionString);
                        }
                        break;
                    case "D":
                    case "d":
                        if(metaPdSM.makeCommand(13,chosenOptionString)){
                            System.out.println(chosenOptionString+" Friend Request Declined!");
                        }else{
                            System.out.println("Impossible to decline the friend request by "+chosenOptionString);
                        }
                        break;
                    case "W":
                    case "w":
                        break;
                    default:
                }
                uiFriendRequests();
                break;
            case "B":
            case "b":
                uiUsers();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiGroups() {
        System.out.println("########################################");
        System.out.println("##### ------------ GROUPS -----------");
        System.out.println("##### G - List Every Group");
        System.out.println("##### R - Group Requests");
        System.out.println("##### C - Create Group");
        System.out.println("##### E - Exit Group");
        System.out.println("##### M - Manage Group");
        System.out.println("##### S - Send Simple Message to Group");
        System.out.println("##### F - Send Message with File to Group");
        System.out.println("##### L - List Messages From My Groups");
        System.out.println("##### T - Delete Message");
        System.out.println("##### ------------------------------");
        System.out.println("##### B - Back");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[GgRrCcEeMmSsFfLlTtBb]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "G":
            case "g":
                if(metaPdSM.makeCommand(14)){
                    System.out.println(metaPdSM.getGroupList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiGroups();
                break;
            case "R":
            case "r":
                uiGroupRequests();
                break;
            case "C":
            case "c":
                System.out.print("Group name: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(19,chosenOptionString)){
                    System.out.println("Group created!");
                }else{
                    System.out.println("Impossible to create the group!");
                }
                uiGroups();
                break;
            case "E":
            case "e":
                System.out.print("Group ID: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(20,chosenOptionString)){
                    System.out.println("You exit the Group!");
                }else{
                    System.out.println("Impossible to exit the group!");
                }
                uiGroups();
                break;
            case "M":
            case "m":
                uiManageGroup();
                break;
            case "S":
            case "s":
                System.out.print("Send to: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("Message: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                if(metaPdSM.makeCommand(24,chosenOptionString,chosenOptionString2)){
                    System.out.println("The message was sent!");
                }else{
                    System.out.println("Impossible to send the message");
                }
                uiGroups();
                break;
            case "F":
            case "f":
                System.out.print("Send to: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("Message: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                System.out.print("File: ");
                sc = new Scanner(System.in);
                chosenOptionString3 = sc.nextLine();
                if(chosenOptionString3 == null || chosenOptionString3.length() < 1){
                    System.out.println("Impossible to manage the file");
                    uiUsers();
                    break;
                }
                try {
                    File file = (File) FileUtility.loadFile(chosenOptionString3);
                    if(metaPdSM.makeCommand(24,chosenOptionString,chosenOptionString2,file)){
                        System.out.println("The message was sent!");
                    }else{
                        System.out.println("Impossible to send the message");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                uiGroups();
                break;
            case "L":
            case "l":
                if(metaPdSM.makeCommand(25)){
                    System.out.println(metaPdSM.getMessageGroupList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiGroups();
                break;
            case "T":
            case "t":
                System.out.print("Delete Message with MessageID: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(8,chosenOptionString)){
                    System.out.println("Message deleted!");
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiGroups();
                break;
            case "B":
            case "b":
                uiCommandPage();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiGroupRequests() {
        System.out.println("########################################");
        System.out.println("##### --------- GROUP REQUESTS --------");
        System.out.println("##### S - Send Group Request");
        System.out.println("##### M - My Groups Request");
        System.out.println("##### A - Answer Group Requests");
        System.out.println("##### ------------------------------");
        System.out.println("##### B - Back");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[SsMmAaBb]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "S":
            case "s":
                System.out.print("Group: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(15,chosenOptionString)){
                    System.out.println("The Friend Request was sent!");
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiGroupRequests();
                break;
            case "M":
            case "m":
                if(metaPdSM.makeCommand(16)){
                    System.out.println("### Group Requests ###");
                    System.out.println(metaPdSM.getGroupRequestList());
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiGroupRequests();
                break;
            case "A":
            case "a":
                System.out.print("Username: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("Group: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                System.out.println("A - Accept");
                System.out.println("D - Decline");
                System.out.println("W - Wait");
                sc = new Scanner(System.in);
                while(!sc.hasNext("[AaDdWw]")){
                    sc.next();
                }
                chosenOptionString3 = sc.next();
                switch (chosenOptionString3){
                    case "A":
                    case "a":
                        if(metaPdSM.makeCommand(17,chosenOptionString,chosenOptionString2)){
                            System.out.println(chosenOptionString+" Group Request Accepted!");
                        }else{
                            System.out.println("Impossible to accept the group request by "+chosenOptionString);
                        }
                        break;
                    case "D":
                    case "d":
                        if(metaPdSM.makeCommand(18,chosenOptionString,chosenOptionString2)){
                            System.out.println(chosenOptionString+" Group Request Declined!");
                        }else{
                            System.out.println("Impossible to decline the group request by "+chosenOptionString);
                        }
                        break;
                    case "W":
                    case "w":
                        break;
                    default:
                }
                uiGroupRequests();
                break;
            case "B":
            case "b":
                uiGroups();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void uiManageGroup() {
        System.out.println("########################################");
        System.out.println("##### --------- MANAGE GROUP --------");
        System.out.println("##### C - Change Group Name");
        System.out.println("##### D - Delete Group");
        System.out.println("##### M - Delete Member from Group");
        System.out.println("##### ------------------------------");
        System.out.println("##### B - Back");
        System.out.println("########################################");
        sc = new Scanner(System.in);
        while(!sc.hasNext("[CcDdMmBb]")){
            sc.next();
        }
        chosenOptionString = sc.next();
        switch(chosenOptionString) {
            case "C":
            case "c":
                System.out.print("Group ID: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("New Group name: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                if(metaPdSM.makeCommand(21,chosenOptionString,chosenOptionString2)){
                    System.out.println("The Group Name Was Changed!");
                }else{
                    System.out.println("Impossible to reach the information");
                }
                uiManageGroup();
                break;
            case "D":
            case "d":
                System.out.print("Group: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                if(metaPdSM.makeCommand(22,chosenOptionString)){
                    System.out.println("The group was deleted!");
                }else{
                    System.out.println("Impossible to delete the group!");
                }
                uiManageGroup();
                break;
            case "M":
            case "m":
                System.out.print("Username: ");
                sc = new Scanner(System.in);
                chosenOptionString = sc.nextLine();
                System.out.print("Group: ");
                sc = new Scanner(System.in);
                chosenOptionString2 = sc.nextLine();
                if(metaPdSM.makeCommand(23,chosenOptionString,chosenOptionString2)){
                    System.out.println(chosenOptionString+" removed from Group!");
                }else{
                    System.out.println("Impossible to remove member from Group");
                }
                uiManageGroup();
                break;
            case "B":
            case "b":
                uiGroups();
                break;
            default: {
                try {
                    throw new InvalidTextUIOptionException();
                } catch (InvalidTextUIOptionException e) {
                    Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

}
