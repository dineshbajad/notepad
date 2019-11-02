import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


class notepad extends Frame{
    Frame mf;
  static  Label frameLabel;
    String filePath="",fileName="";
    public notepad(){
        mf=this;
         addWindowListener(new MyWindowAdapter());

        setFont(new Font("Serif",Font.BOLD,15));

        setLayout(new BorderLayout());
        //setting textarea
        JTextArea textarea=new JTextArea();
        JScrollPane sp = new JScrollPane(textarea);   // JTextArea is placed in a JScrollPane.
        add(sp);
        //setting menubar
        MenuBar mbar=new MenuBar();
        setMenuBar(mbar);

        Menu file=new Menu("Menu");
        MenuItem item1,item2,item3,item4,item5;
        file.add(item1=new MenuItem("New",new MenuShortcut(KeyEvent.VK_N,false)));
        file.add(item2=new MenuItem("Open",new MenuShortcut(KeyEvent.VK_O,false)));
        file.add(item3=new MenuItem("Save",new MenuShortcut(KeyEvent.VK_S,false)));
        file.add(item4=new MenuItem("Save As",new MenuShortcut(KeyEvent.VK_S,true)));
        file.add(item5=new MenuItem("Close",new MenuShortcut(KeyEvent.VK_Q,false)));
        mbar.add(file);

        Menu view =new Menu("View");
        MenuItem itemv1,itemv2;
        view.add(itemv1=new MenuItem("Dark",new MenuShortcut(KeyEvent.VK_D,true)));
        view.add(itemv2=new MenuItem("Light",new MenuShortcut(KeyEvent.VK_L,true)));
        mbar.add(view);
        Menu edit=new Menu("Edit");
        MenuItem itemo1,itemo2,itemo3;
        edit.add(itemo1=new MenuItem("Copy",new MenuShortcut(KeyEvent.VK_C,false)));
        edit.add(itemo2=new MenuItem("Paste",new MenuShortcut(KeyEvent.VK_V,false)));
        edit.add(itemo3=new MenuItem("Cut",new MenuShortcut(KeyEvent.VK_X,false)));
        mbar.add(edit);

        Choice fontsList=new Choice();
        Choice fontsSize=new Choice();
        GraphicsEnvironment ge;
        ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fonts[]=ge.getAvailableFontFamilyNames();
        for(int i=0;i<fonts.length;i++){
             fontsList.add(fonts[i]);
         }
        for(int i=10;i<=100;i+=2) {
            String ans=String.valueOf(i);
            fontsSize.add(ans);
        }

         Panel fontPanel=new Panel();
        add(fontPanel,BorderLayout.NORTH);
        fontPanel.setPreferredSize(new Dimension(mf.getWidth(),35));
        fontPanel.add(fontsList);
        fontPanel.add(fontsSize);

        MyActionHandler handler=new MyActionHandler(textarea,fontsList,fontsSize);
        item1.addActionListener(handler);
        item2.addActionListener(handler);
        item3.addActionListener(handler);
        item4.addActionListener(handler);
        item5.addActionListener(handler);
        itemv1.addActionListener(handler);
        itemv2.addActionListener(handler);
        fontsList.addItemListener(handler);
        fontsSize.addItemListener(handler);
        itemo1.addActionListener(handler);
        itemo2.addActionListener(handler);
        itemo3.addActionListener(handler);

    }

//     public void paint(Graphics g)
//     {
//         g.drawString(msg,100,100);
//     }
    class MyActionHandler implements ActionListener,ItemListener{
        JTextArea textarea;
        Choice fontsSize,fontsList;
        public  MyActionHandler(JTextArea textarea,Choice fontsList,Choice fontsSize) {
            this.textarea = textarea;
            this.fontsList=fontsList;
            this.fontsSize=fontsSize;
        }
        public void actionPerformed(ActionEvent ae){
            String button=ae.getActionCommand();
            if(button.equals("New")){
                textarea.setText("");
            }
            if(button.equals("Open"))
            {
                FileDialog fd=new FileDialog(mf,"Open File",FileDialog.LOAD);
                fd.setVisible(true);
                try{
                    filePath=fd.getDirectory();
                    fileName=fd.getFile();
                    FileInputStream in =new FileInputStream(filePath+fileName);
                    String filedata="";
                    int ch;
//                    for(int i=0;i<in.available();i++)
//                        filedata+=(char)in.read();
                    while((ch=in.read())!=-1)
                        filedata+=(char)(ch);
                     in.close();
                    textarea.setText(filedata);
                    mf.setTitle("Notepad/"+fileName);
                    frameLabel.setText("File Opened Successfully");

                }catch(IOException e)
                {
                    System.out.println("I/O exception");
                }

            }
            else if(button.equals("Save")){
                String newText=textarea.getText();
                 try{
                    FileOutputStream fo=new FileOutputStream(filePath+fileName);
                    for(int i=0;i<newText.length();i++) {
                        fo.write((char)newText.charAt(i));
                    }
                    fo.close();
                     frameLabel.setText("File Updated Successfully");
                 }catch(IOException e)
                {
                    frameLabel.setText("Unable to Save");
                }
            }
            else if(button.equals("Save As"))
            {
                FileDialog fd=new FileDialog(mf,"Save File",FileDialog.SAVE);
                fd.setVisible(true);
                 try{
                     String  saveFilePath,saveFileName,newText;
                     saveFileName=fd.getFile();
                     saveFilePath=fd.getDirectory();
                     newText=textarea.getText();
                     FileOutputStream fo=new FileOutputStream(saveFilePath+saveFileName);
                     for(int i=0;i<newText.length();i++) {
                         fo.write((char) newText.charAt(i));
                     }
                     fo.close();
                     frameLabel.setText("File Saved Successfully");
                 }catch(Exception e){
                     frameLabel.setText("Unable to create new file");
                 }
//                JFileChooser j=new JFileChooser(filePath);
//                j.showSaveDialog(mf);

            }
             else if(button.equals("Dark")) {
                 textarea.setBackground(Color.BLACK);
                 textarea.setForeground(Color.GREEN);
             }
            else  if(button.equals("Light")) {
                 textarea.setBackground(Color.WHITE);
                 textarea.setForeground(Color.BLACK);
             }
            else if(button.equals("Close"))
                System.exit(0);
            else if(button.equals("Copy"))
            {
               textarea.copy();
             }
            else if(button.equals("Cut"))
            {
                textarea.cut();
            }    else if(button.equals("Paste"))
            {
                textarea.paste();
            }
        }
        public void itemStateChanged(ItemEvent ie){
           String font=fontsList.getSelectedItem();
           int fontSize=Integer.parseInt(fontsSize.getSelectedItem());
           textarea.setFont(new Font(font,Font.PLAIN,fontSize));
        }
    }
}
public class notepadapp{
    public static void main(String args[]){
        notepad app=new notepad();
        app.setSize(new Dimension(1366,720));
        app.setTitle("Notepad");
        Panel statuspanel=new Panel();
        app.add(statuspanel,BorderLayout.SOUTH);
        statuspanel.setPreferredSize(new Dimension(app.getWidth(),25));
        Label statusLabel=new Label("status");
        statusLabel.setPreferredSize(statuspanel.getPreferredSize());
        statusLabel.setAlignment(Label.CENTER);
        statuspanel.add(statusLabel);
        app.setVisible(true);
        app.frameLabel=statusLabel;
    }
}
class MyWindowAdapter extends WindowAdapter{
    public void windowClosing(WindowEvent we)
    {
        System.exit(0);
    }
}
