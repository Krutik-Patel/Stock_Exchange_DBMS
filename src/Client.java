
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
  public static void main(String[] args) throws Exception {
  try{
    Socket socket=new Socket("127.0.0.1",8888);
    DataInputStream inStream=new DataInputStream(socket.getInputStream());
    DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    String clientMessage="",serverMessage="";



      	String login_acc_id = "-";

        Scanner sc = new Scanner(System.in);
        boolean out = true;
    while(out){

		

		while(login_acc_id .equals("-")){

			System.out.print("Already have an account (y / n): ");
			String register = sc.next();

			if(register.equals("y")){
				System.out.print("Account ID : ");
				String acc_id = sc.next();
				System.out.print("Password : ");
				String pass = sc.next();

        clientMessage = "1 "+acc_id+" "+pass;

        //SERVER_COMMUNICATION
        outStream.writeUTF(clientMessage);
        outStream.flush();
        serverMessage=inStream.readUTF();
				//
            
        String[] msg = serverMessage.split("[$]");

				if( msg[0].equals("YES")){
					login_acc_id = acc_id;
					break;
				}
				else{
					System.out.println(" -- Wrong ID or PASSWORD --");
					continue;
				} 
					
			}
			else{

				String holder_regs_id = "-";
				System.out.print("Register User (u) / Company (c): ");
				String part_choice = sc.next();
				if(part_choice.equals("u")){
						// 1 - Register User
					System.out.print("First name : ");
					String fname = sc.next();
					System.out.print("Middle name : ");
					String mname = sc.next();
					System.out.print("Last name : ");
					String lname = sc.next();
					System.out.print("Date of birth(dd/MM/yyyy) : ");
					String dob = sc.next();
					System.out.print("Pan number : ");
					String panNo = sc.next();

          clientMessage = "3 "+fname+" "+mname+" "+lname+" "+dob+" "+panNo;
					

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           String[] msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
				    	holder_regs_id = msg[1];
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	continue;
				    } 

					
				
				}
				else if(part_choice.equals("c")){
						// 2 - Register Company
					System.out.print("Company name : ");
					String companyName = sc.next();
					System.out.print("Pan number : ");
					String panNo2 = sc.next();


          clientMessage = "4 "+companyName+" "+panNo2;
					


           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           String[] msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
				    	holder_regs_id = msg[1];
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	continue;
				    } 

				
				}

				else continue;

				if(holder_regs_id.equals("-")){
					continue;
				}
				

				System.out.println(" -- SignUP Account --");
				System.out.print("SET Password: ");
				String password = sc.next();
				System.out.print("Bank Account no.: ");
				String bank_acc_no = sc.next();
				String acc_id;

         clientMessage = "2 "+holder_regs_id+" "+password+" "+bank_acc_no;
					
           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           String[] msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
				    	acc_id = msg[1];
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	continue;
				    } 


				System.out.println(" -- Your Account ID: "+acc_id+" --");
        
				clientMessage = "1 "+acc_id+" "+password;

        //SERVER_COMMUNICATION
        outStream.writeUTF(clientMessage);
        outStream.flush();
        serverMessage=inStream.readUTF();
				//

        msg = serverMessage.split("[$]");

				if( msg[0].equals("YES")){
					login_acc_id = acc_id;
					break;
				}
				else{
					System.out.println(" -- Wrong ID or PASSWORD --");
					continue;
				} 
			}

			

			
		}

    String[] msg;
		System.out.println("1. Stock Analysis\n2. Buy Stocks\n3. Sell Stocks\n4. Market Trends\n5. Transaction History\n6. Index\n7. Exit");
		System.out.print("Usecase Type : ");

		int usecaseType = sc.nextInt();
		switch(usecaseType){
			

			case 1 :
			// 3 - Analysis of particular stock
				System.out.print("Stock ID : ");
				String stockId3 = sc.next();
				System.out.print("Date(yyyy-mm-dd): ");
				String date = sc.next();

        clientMessage = "5 "+stockId3+" "+date;
				

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
              for(int i=1;i<msg.length;i++){
                  System.out.println(msg[i]);
              }
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	
				    } 
				
				break;

			case 2 :
			// 4 - Buy stocks
				System.out.print("Stock ID : ");
				String stockId4 = sc.next();
				System.out.print("No. of stock units to buy : ");
				String stockUnits4 = sc.next();

         clientMessage = "6 "+login_acc_id+" "+stockId4+" "+stockUnits4;
				

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
              for(int i=1;i<msg.length;i++){
                  System.out.println(msg[i]);
              }
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	
				    } 

				
				break;

			case 3 :
			// 5 - Sell stocks
				System.out.print("Stock ID : ");
				String stockId5 = sc.next();
				System.out.print("No. of stock units to sell : ");
				String stockUnits5 = sc.next();
				
         clientMessage = "7 "+login_acc_id+" "+stockId5+" "+stockUnits5;
				

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
              for(int i=1;i<msg.length;i++){
                  System.out.println(msg[i]);
              }
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	
				    } 

				break;

			case 4 :
			// 6 - Market trends

        System.out.print("Parameter for Market trend(M(Month)/D(Day)/Y(Year)/W(Week)) : ");
				String parTrend = sc.next();

         clientMessage = "8 "+parTrend;
				

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //


           msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
              for(int i=1;i<msg.length;i++){
                  System.out.println(msg[i]);
              }
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	
				    } 
				
				break;

			case 5 :
			// 7 - User transaction history
				
         clientMessage = "9 "+login_acc_id;
				

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //

           msg = serverMessage.split("[$]");

				    if( msg[0].equals("YES")){
              for(int i=1;i<msg.length;i++){
                  System.out.println(msg[i]);
              }
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	
				    } 
				
				break;

			case 6: 
			// 8 - Index of domain wise stocks
				clientMessage = "10";
				

           //SERVER_COMMUNICATION
          outStream.writeUTF(clientMessage);
          outStream.flush();
          serverMessage=inStream.readUTF();
				  //

           msg = serverMessage.split("[$]");


					
				    if( msg[0].equals("YES")){
              for(int i=1;i<msg.length;i++){
                  System.out.println(msg[i]);
              }
				    	
				    }
				    else{
				    	System.out.println(" -- Something went wrong --");
				    	
				    } 

			break;

      case 7 :
          out = false;

		  break;

			default :
				System.out.print("Wrong usecase type! \n");
        
		}
		



    }

    sc.close();
    outStream.close();
    outStream.close();
    socket.close();
  }catch(Exception e){
    System.out.println(e);
  }
  }
}