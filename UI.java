import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UI extends JFrame{

    //all the variables needed for the jframe
    private DefaultListModel<String> songList = new DefaultListModel<String>();
    private JPanel container = new JPanel();
    private JButton playBtn = new JButton();
    private JButton addBtn = new JButton();
    private JButton nextBtn = new JButton();
    private JButton previousBtn = new JButton();
    private JButton delBtn = new JButton();
    private JList<String> jsongList = new JList<String>();
    private JLabel lblplaying = new JLabel();
    private JFileChooser fc = new JFileChooser();
    SeekBar seekbar = new SeekBar();
    AudioGuts ag = null;

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UI ui = new UI();
                ui.setVisible(true);
            }
        });
    }

    public UI(){
        init();
        uiBehavior();
    }

    public void init(){

        setTitle("Michael's Mp3 Player");

        int height = 600;
        int width = 660;

        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Container settings
        container.setLayout(null);
        getContentPane().add(container);

        //Buttons
        int btnHeight = 35;
        int line1 = 80;

        JPanel contBtn = new JPanel();
        contBtn.setBounds(0, line1, 250, btnHeight);

        previousBtn.setText("<<");
        previousBtn.setBounds(0, 0, 50, btnHeight);

        playBtn.setText(">");
        playBtn.setMnemonic(KeyEvent.VK_SPACE);
        playBtn.setBounds(0, 0, 50, btnHeight);

        nextBtn.setText(">>");
        nextBtn.setBounds(0, 0, 50, btnHeight);

        addBtn.setText("add");
        addBtn.setBounds(width-80, line1, 70, btnHeight);

        contBtn.add(previousBtn);
        contBtn.add(playBtn);
        contBtn.add(nextBtn);

        container.add(contBtn);
        container.add(addBtn);
        //Playing Panel
        JPanel panelNP = new JPanel();
        panelNP.setLayout(new BoxLayout(panelNP, BoxLayout.PAGE_AXIS));
        panelNP.setToolTipText("Now Playing");
        panelNP.setBorder(BorderFactory.createMatteBorder(1, 0, 2, 0, Color.gray));
        panelNP.setBounds(5, line1-25, width-15, 20);

        lblplaying.setText("Now Playing : ");
        lblplaying.setBounds(5, 0, 100, 40);

        panelNP.add(lblplaying);
        container.add(panelNP);

        //songlist
        int listHeight = 100;
        JScrollPane listScroller = new JScrollPane(jsongList);
        listScroller.setPreferredSize(new Dimension(width-10, listHeight));
        listScroller.setBounds(10, line1+50, width-20, listHeight+150);
        container.add(listScroller);

        //2 row buttongs
        int line2 = line1+listHeight+50;
        //Del Button
        delBtn.setBounds(width-80, line2+155, 70, 30);
        delBtn.setText("delete");
        container.add(delBtn);

        seekbar.setBounds(5, 10, width-15, 10);
        container.add(seekbar);
    }

    private void uiBehavior(){
        fc.setMultiSelectionEnabled(true);

        ag = new AudioGuts();

        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()){
                    return true;
                }
                if(f.getName().endsWith(".mp3")){
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "only supports mp3";
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(addBtn);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File[] files = fc.getSelectedFiles();
                    for(File f : files){
                        ag.addSong(f.getAbsolutePath());
                        songList.addElement(f.getName());
                        System.out.println("Added "+f.getName()+" to file");
                    }
                }else{
                    System.out.println("no file selected");
                }
            }
        });

        jsongList.setModel(songList);
        jsongList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jsongList.setLayoutOrientation(JList.VERTICAL);

        jsongList.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent evt){
                JList list = (JList)evt.getSource();
                if(evt.getClickCount() == 2){
                    int index = list.locationToIndex(evt.getPoint());
                    ag.setIndexSong(index);
                    try {
                        ag.play();
                    } catch (Exception ev) {
                        ev.printStackTrace();
                    }
                }
            }
        });

        //Btn Delete
        delBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Executed Outside UI Thread
                BackgroundExecutor.get().execute(new Runnable() {

                    @Override
                    public void run() {
                        int[] indexes = jsongList.getSelectedIndices();
                        int removed = 0;
                        for(int i : indexes)
                        {
                            System.out.println("Removed Song ("+(i-removed)+")" + songList.get(i-removed));
                            ag.removeSong(i-removed);
                            songList.remove(i-removed);
                            removed++;
                        }
                    }
                });
            }
        });

        //Play Btn
        playBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tooglePlay();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        previousBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    ag.prvSong();
                } catch (Exception e) {
                    System.out.println("error calling the previous song");
                    e.printStackTrace();
                }
            }
        });
    }

    private void tooglePlay() {
        if (songList.size() == 0)
            return;
        if (ag.isPaused()) {
            System.out.println("playing");
            ag.play();
        } else {
            System.out.println("paused");
            ag.pause();
        }
    }

}
