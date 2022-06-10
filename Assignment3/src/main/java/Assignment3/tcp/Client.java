package Assignment3.tcp;


import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.*;

public class Client {
  /*
   * request: { "selected": <int: 1=joke, 2=quote, 3=image, 4=random>,
   * (optional)"min": <int>, (optional)"max":<int> }
   * 
   * response: {"datatype": <int: 1-string, 2-byte array>, "type": <"joke",
   * "quote", "image"> "data": <thing to return> }
   * 
   * error response: {"error": <error string> }
   */



static int score;

//Asks and checks for answer

  public static void getAnswer() throws IOException {
    System.out.println("Who said this quote?: ");
    Scanner ans = new Scanner(System.in);
    if(ans.hasNext("wolverine")){
      String guess = ans.next();
      System.out.println("your guess "+guess+" was correct!");
      score++;
      System.out.println("Score: "+score);
    } else{
      System.out.println("Incorrect, please try again!");
    }
    if(score >= 4) {
      youWin();
    }
  }
    // Display Winning window
    public static void youWin() throws IOException{



      File win = new File("img/win.jpg");
      BufferedImage bufferedImage = ImageIO.read(win);
      System.out.println("Congrats You Won!!!!");

      ImageIcon imageIcon = new ImageIcon(bufferedImage);
      JFrame jFrame = new JFrame();

      jFrame.setLayout(new FlowLayout());

      jFrame.setSize(300,300);
      JLabel jLabel = new JLabel();

      jLabel.setIcon(imageIcon);
      jFrame.add(jLabel);
      jFrame.setVisible(true);

      jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  public static JSONObject image() {
    JSONObject request = new JSONObject();
    request.put("selected", 1);
    return request;
  }

  public static JSONObject random() {
    JSONObject request = new JSONObject();
    request.put("selected", 4);
    return request;
  }

  public static void main(String[] args) throws IOException {
    Socket sock;
    try {
      sock = new Socket("localhost", 8080);
      OutputStream out = sock.getOutputStream();
      InputStream in = sock.getInputStream();

      Scanner input = new Scanner(System.in);
      Scanner name = new Scanner(System.in);
      int choice;
      System.out.println("Player 1 what is your name?");
        String playerName = name.nextLine();
      System.out.println("Hello "+playerName+ "!  select 1 to play the Quote Guesser ... Press 0 to disconnect");
      do {
        choice = input.nextInt();
        JSONObject request = null;
        switch (choice) {
        case (1):
          request = image();
          break;
        case (0):
          sock.close();
          out.close();
          in.close();
          System.exit(0);
          break;
        default:
          System.out.println(playerName+" Please select a valid option 1 or 0.");
          break;
        }



        if (request != null) {
          NetworkUtils.Send(out, JsonUtils.toByteArray(request));
          byte[] responseBytes = NetworkUtils.Receive(in);
          JSONObject response = JsonUtils.fromByteArray(responseBytes);
          if (response.has("error")) {
            System.out.println(response.getString("error"));
          } else {
            switch (response.getInt("datatype")) {
            case (1): {
              System.out.println("Your image");
              Base64.Decoder decoder = Base64.getDecoder();
              byte[] bytes = decoder.decode(response.getString("data"));
              ImageIcon icon = null;

              try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
                BufferedImage image = ImageIO.read(bais);
                icon = new ImageIcon(image);
              }
              if (icon != null) {
                JFrame frame = new JFrame();
                JLabel label = new JLabel();
                label.setIcon(icon);
                frame.add(label);
                frame.setSize(icon.getIconWidth(), icon.getIconHeight());
                frame.show();
                getAnswer();
              }
            }
              break;
            }
          }
        }
        System.out.println("Select 1 for next Round ... 0 to end game");
      } while (true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}