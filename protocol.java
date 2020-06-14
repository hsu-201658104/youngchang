package I_P;

import java.util.Scanner;

public class protocol {
	static int l = 28;
	static String next = "";
	static String[] Ethernet = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int k = 0; k < 6; k++) {
			if (l == 28) {
				System.out.print("이더넷 프레임을 입력하시오");
				Scanner scanner = new Scanner(System.in);
				String First = scanner.nextLine();
				String[] Ethernet2 = First.split("");
				Ethernet = Ethernet2;

				// Ethernet Start!!
				String[] DA = new String[12]; // Destination Address
				String[] SA = new String[12]; // Source Address
				String[] type = new String[4]; // Ethernet Type
				for (int i = 0; i < 12; i++) {
					DA[i] = Ethernet[i];
					SA[i] = Ethernet[i + 12];
				}
				for (int i = 0; i < 4; i++) {
					type[i] = Ethernet[i + 24];

				}
				System.out.print("Destination Address >> ");
				String DAM = "";
				for (int i = 0; i < DA.length; i++) {
					if (i == 0) {
						System.out.print(DA[i]);
					} else {
						if (i % 2 == 1) {
							System.out.print(DA[i]);
						} else {
							System.out.print(":" + DA[i]);
						}
					}
					DAM += DA[i];
				}
				if (DAM.equals("ffffffffffff")) {
					System.out.println(" / Broadcast");
				} else if (Integer.parseInt(DA[1], 16) % 2 == 0) {
					System.out.println(" / Unicast");
				} else if (Integer.parseInt(DA[1], 16) % 2 == 1) {
					System.out.println(" / Multicast");
				}

				System.out.print("Source Address >> ");
				for (int i = 0; i < SA.length; i++) {
					if (i == 0) {
						System.out.print(SA[i]);
					} else {
						if (i % 2 == 1) {
							System.out.print(SA[i]);
						} else {
							System.out.print(":" + SA[i]);
						}
					}
				}
				if (Integer.parseInt(SA[1], 16) % 2 == 0) {
					System.out.println(" / Unicast");
				} else if (Integer.parseInt(SA[1], 16) % 2 == 1) {
					System.out.println(" / Multicast");
				} else {
					System.out.println(" / Broadcast");
				}

				System.out.print("Ethernet type >> ");
				for (int i = 0; i < type.length; i++) {
					System.out.print(type[i]);
					next += type[i];
				}
				if (next.equals("0800"))
					IP();
				else if (next.equals("0806"))
					ARP();

				// Ethernet end!!

			}
		}
	}

	private static void IP() {
		// TODO Auto-generated method stub
		System.out.println(" IP\n--- IP ---");
		System.out.println("Version: 0" + Ethernet[l]);
		String IP_HL = Ethernet[l + 1];
		System.out.print("Header Length : " + IP_HL + " / " + Integer.parseInt(IP_HL) * 4 + " bytes ");
		if (IP_HL.equals("5")) {
			System.out.println(" : No-Option");
		} else
			System.out.println(" : Contain Option ");
		String IP_ST = Ethernet[l + 2] + Ethernet[l + 3];
		System.out.print("Service Type : " + IP_ST);
		String ST1 = Integer.toBinaryString(Integer.parseInt(Ethernet[l + 2], 16));
		String ST2 = Integer.toBinaryString(Integer.parseInt(Ethernet[l + 3], 16));
		if (Integer.parseInt(Ethernet[l + 3], 16) < 2)
			ST2 = "0" + Integer.parseInt(Ethernet[l + 3], 16);
		String Precedence = ST1 + ST2;
		if (Precedence.equals("000")) {
			System.out.println(" / No service type");
		} else if (IP_ST.equals("001")) {
			System.out.println(" / Priority");
		} else if (IP_ST.equals("010")) {
			System.out.println(" / Immediate");
		} else if (IP_ST.equals("011")) {
			System.out.println(" / Flash");
		} else if (IP_ST.equals("100")) {
			System.out.println(" / Flash Override");
		} else if (IP_ST.equals("101")) {
			System.out.println(" / Critical");
		} else if (IP_ST.equals("110")) {
			System.out.println(" / Internet work Control");
		} else if (IP_ST.equals("111")) {
			System.out.println(" / Network Control");
		}

		l = l + 4;
		String IP_TL = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		int iptl = Integer.parseInt(IP_TL, 16);
		System.out.print("total length : " + IP_TL + " / " + iptl + " bytes");

		for (int i = 0; i < 16; i++) {
			if (iptl > Math.pow(2, i)) {
				continue;
			} else {
				System.out.println(": " + Math.pow(2, i - 1) + "bytes payload");
				break;
			}
		}

		l = l + 4;
		String IP_I = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Identification : " + IP_I + " / " + Integer.parseInt(IP_I, 16));

		l = l + 4;
		int IP_F = Integer.parseInt(Ethernet[l]);
		String flag = "";
		if (IP_F >= 8)
			flag = Integer.toBinaryString(IP_F);
		else if (IP_F >= 4)
			flag = "0" + Integer.toBinaryString(IP_F);
		else if (IP_F >= 2)
			flag = "00" + Integer.toBinaryString(IP_F);
		else
			flag = "000" + Integer.toBinaryString(IP_F);

		System.out.println("Flag : " + IP_F + " / " + flag);
		String[] F = flag.split("");
		System.out.println("-Reserve : " + F[0]);
		System.out.print("-Don't Fragment : " + F[1]);
		if (F[1].equals("0"))
			System.out.println(" / Able to fragment");
		else
			System.out.println(" / Unable to fragment");
		System.out.print("-More : " + F[2] + F[3]);
		if ((Integer.parseInt(F[2]) + Integer.parseInt(F[3])) == 0) {
			System.out.println("/ No more fragments");
		} else
			System.out.println(" / more fragments");

		l = l + 1;
		System.out.print("Offset: " + Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2]);
		if ((Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2]).equals("000")) {
			System.out.println(" / First Fragment");
		} else {
			System.out.println(
					" / " + Integer.parseInt(Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2], 16) + " Fragment");
		}

		l = l + 3;
		String IP_TTL = Ethernet[l] + Ethernet[l + 1];
		System.out.println("TTL : " + IP_TTL + " / " + Integer.parseInt(IP_TTL, 16) + " hops");

		l = l + 2;
		String IP_PRO = Ethernet[l] + Ethernet[l + 1];
		System.out.print("Protocol : " + IP_PRO);
		if (IP_PRO.equals("06")) {
			System.out.println(" / TCP");
			next = IP_PRO;
		} else if (IP_PRO.equals("01")) {
			System.out.println(" / ICMP");
			next = IP_PRO;
		} else if (IP_PRO.equals("02")) {
			System.out.println(" / IGMP");
			next = IP_PRO;
		} else if (IP_PRO.equals("17")) {
			System.out.println(" / UDP");
			next = IP_PRO;
		} else if (IP_PRO.equals("89")) {
			System.out.println(" / OSPF");
			next = IP_PRO;
		}

		l = l + 2;
		String IP_C = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Checksum : " + IP_C);

		l = l + 4;
		String IP_SA = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7];
		System.out.println("Source Adress: " + IP_SA + " / " + Integer.parseInt(Ethernet[l] + Ethernet[l + 1], 16) + "."
				+ Integer.parseInt(Ethernet[l + 2] + Ethernet[l + 3], 16) + "."
				+ Integer.parseInt(Ethernet[l + 4] + Ethernet[l + 5], 16) + "."
				+ Integer.parseInt(Ethernet[l + 6] + Ethernet[l + 7], 16));

		l = l + 8;
		String IP_DA = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7];
		System.out.println("Source Adress: " + IP_DA + " / " + Integer.parseInt(Ethernet[l] + Ethernet[l + 1], 16) + "."
				+ Integer.parseInt(Ethernet[l + 2] + Ethernet[l + 3], 16) + "."
				+ Integer.parseInt(Ethernet[l + 4] + Ethernet[l + 5], 16) + "."
				+ Integer.parseInt(Ethernet[l + 6] + Ethernet[l + 7], 16));

		if (next.equals("06")) {
			TCP();
		} else if (next.equals("01")) {
			ICMP();
		} else if (next.equals("02")) {
			IGMP();
		} else if (next.equals("17")) {
			UDP();
		} else if (next.equals("89")) {
			OSPF();
		}
	}

	private static void OSPF() {
		// TODO Auto-generated method stub
		System.out.println("--- OSPF ---");
	}

	private static void UDP() {
		// TODO Auto-generated method stub
		System.out.println("--- UDP ---");
		l = l + 8;
		String UDP_SP = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		int SP_P = Integer.parseInt(UDP_SP, 16);
		System.out.println("Source port : " + UDP_SP + " / " + SP_P);
		
		if (SP_P == 7) {
			System.out.println("-Protocol : Echo(Echoes back a received datagram)");
		} else if (SP_P == 9) {
			System.out.println("-Protocol : Discard(Discard any datagram that is received)");
		} else if (SP_P == 11) {
			System.out.println("-Protocol : Users(Active Users)");
		} else if (SP_P == 13) {
			System.out.println("-Protocol : Daytime(Returns the date and the time)");
		} else if (SP_P == 17) {
			System.out.println("-Protocol : Quote(Returns a quote of the day)");
		} else if (SP_P == 19) {
			System.out.println("-Protocol : Chargen(Returns a string of characters)");
		} else if (SP_P == 53) {
			System.out.println("-Protocol : DNS(Domain Name Service)");
		} else if (SP_P == 67) {
			System.out.println("-Protocol : Dynamic Host Configuration Protocol");
		} else if (SP_P == 69) {
			System.out.println("-Protocol : TFTP(Trivial File Transfer Protocol)");
		} else if (SP_P == 111) {
			System.out.println("-Protocol : RPC(Remote Procedure Call)");
		} else if (SP_P == 123) {
			System.out.println("-Protocol : NTP(Network Time Protocol)");
		} else if (SP_P == 161) {
			System.out.println("-Protocol : SNMP-sercer(Simple Network Management Protocol)");
		} else if (SP_P == 162) {
			System.out.println("-Protocol : SNMP-client(Simple Network Management Protocol)");
		}
		l=l+4;
		String UDP_DP = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		int DP_P = Integer.parseInt(UDP_DP, 16);
		System.out.println("Destination port : " + UDP_DP + " / " + DP_P);
		l=l+4;
		String UDP_TL =  Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		int TL = Integer.parseInt(UDP_TL, 16);
		System.out.println("Total length : " + UDP_TL + " / " + TL + " bytes");
		l=l+4;
		int DL = TL - 8;
		System.out.println("Data length : " + DL + " bytes");
		String UDP_CS = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Checksum : " + UDP_CS);
		
		
	}

	private static void IGMP() {
		// TODO Auto-generated method stub
		System.out.println("--- IGMP ---");
	}

	private static void ICMP() {
		// TODO Auto-generated method stub
		System.out.println("--- ICMP ---");
		l = l + 8;
		String ICMP_T = Ethernet[l] + Ethernet[l + 1];
		l = l + 2;
		String ICMP_C = Ethernet[l] + Ethernet[l + 1];
		System.out.print("Type : " + ICMP_T + " Code : " + ICMP_C + " / ");
		String TC = ICMP_T + ICMP_C;
		if (ICMP_T.equals("03")) {
			System.out.print("Destination Unreachable ");
			if (ICMP_C.equals("00")) {
				System.out.println(" - Network Unreachable");
			} else if (ICMP_C.equals("01")) {
				System.out.println(" - Host Unreachable");
			} else if (ICMP_C.equals("02")) {
				System.out.println(" - Protocol Unreachable");
			} else if (ICMP_C.equals("06")) {
				System.out.println(" - Destination Network Unknown");
			} else if (ICMP_C.equals("07")) {
				System.out.println(" - Destination Host Unknown");
			} else {
				System.out.println("");
			}
		} else if (ICMP_T.equals("04")) {
			System.out.println("Source quench");
		} else if (ICMP_T.equals("05")) {
			System.out.print("Redirection");
			if (ICMP_C.equals("00")) {
				System.out.println(" - Redirect Datagram for the Network");
			} else if (ICMP_C.equals("01")) {
				System.out.println(" - Redirect Datagram for the Host");
			} else {
				System.out.println("");
			}
		} else if (ICMP_T.equals("11")) {
			System.out.print("Time exceeded");
			if (ICMP_C.equals("00")) {
				System.out.println(" - Time to Live exceeded in Transit");
			} else if (ICMP_C.equals("01")) {
				System.out.println(" - Fragment Reassembly Time Exceeded");
			}
		} else if (ICMP_T.equals("12")) {
			System.out.print("Parameter problem");
			if (ICMP_C.equals("00")) {
				System.out.println(" - Invalid IP header.");
			} else if (ICMP_C.equals("01")) {
				System.out.println(" - A required option is missing");
			} else
				System.out.println("");
		} else if (ICMP_T.equals("08") || ICMP_T.equals("00")) {
			System.out.println("Echo request and reply");
		} else if (ICMP_T.equals("13") || ICMP_T.equals("14")) {
			System.out.println("Timestamp request and reply");
		}

		l = l + 2;
		String ICMP_CS = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("CheckSum : " + ICMP_CS);
		l = l + 4;
		String ICMP_I = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Identifier : " + ICMP_I);

		l = l + 4;
		String ICMP_SN = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Sequence number : " + ICMP_SN);
		l=l+4;
		String ICMP_DS = "";
		for(int i=l; i<Ethernet.length; i++) {
			ICMP_DS +=Ethernet[i];
		}
		System.out.println("Data section(Query messages) : " +ICMP_DS);
		
	}

	private static void TCP() {
		// TODO Auto-generated method stub
		System.out.println("--- TCP ---");

		l = l + 8;
		String TCP_SP = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		int sp = Integer.parseInt(TCP_SP, 16);
		System.out.print(" Source Port : " + TCP_SP + " / ");
		if (sp < Math.pow(2, 10)) {
			if (sp == 20 || sp == 21) {
				System.out.println(sp + " Well-Known Port (FTP)");
			} else if (sp == 22) {
				System.out.println(sp + " Well-Known Port (SSH)");
			} else if (sp == 23) {
				System.out.println(sp + " Well-Known Port (Telnet)");
			} else if (sp == 25) {
				System.out.println(sp + " Well-Known Port (SMTP)");
			} else if (sp == 80) {
				System.out.println(sp + " Well-Known Port (HTTP)");
			} else if (sp == 110) {
				System.out.println(sp + " Well-Known Port (POP3)");
			} else if (sp == 143) {
				System.out.println(sp + " Well-Known Port (IMAP4)");
			} else
				System.out.println(sp + " Well-Known Port");
		} else if (sp < Math.pow(2, 14) + Math.pow(2, 15) - 1) {
			System.out.println(sp + " Registered Port");
		} else {
			System.out.println(sp + " Dynamic Port");
		}

		l = l + 4;
		String TCP_DP = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Destination Port : " + TCP_DP + " / " + Integer.parseInt(TCP_DP, 16) + " : Client Port");

		l = l + 4;
		String TCP_SN = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7];
		System.out.println("Sequence number : " + TCP_SN);

		l = l + 8;
		String TCP_AN = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7];
		System.out.println("Ack number : " + TCP_AN);
		// l=92;
		l = l + 8;
		String TCP_HL = Ethernet[l];
		System.out.println("Header Length : " + TCP_HL + " / " + Integer.parseInt(TCP_HL) * 4 + "bytes");

		l = l + 1;
		String TCP_CB = Ethernet[l + 1] + Ethernet[l + 2];

		int cb1 = Integer.parseInt(Ethernet[l + 1], 16);
		int cb2 = Integer.parseInt(Ethernet[l + 2], 16);

		String CB1 = "";
		if (cb1 < 2) {
			CB1 += "000" + Integer.toBinaryString(cb1);
		} else if (cb1 < 4) {
			CB1 = "00" + Integer.toBinaryString(cb1);
		} else if (cb1 < 8) {
			CB1 = "0" + Integer.toBinaryString(cb1);
		} else
			CB1 = Integer.toBinaryString(cb1);
		String CB2 = "";
		if (cb2 < 2) {
			CB2 += "000" + Integer.toBinaryString(cb2);
		} else if (cb2 < 4) {
			CB2 = "00" + Integer.toBinaryString(cb2);
		} else if (cb2 < 8) {
			CB2 = "0" + Integer.toBinaryString(cb2);
		} else
			CB2 = Integer.toBinaryString(cb2);

		String cbB = CB1 + CB2;

		String[] controlfield = cbB.split("");
		System.out.println("Control bits : " + TCP_CB + " / " + cbB);
		System.out.print("- Urgent : " + controlfield[2] + " / ");
		if (controlfield[2].equals("0"))
			System.out.println(" Not Urgent");
		else
			System.out.println(" Urgent pointer is valid");
		System.out.print("- ACK : " + controlfield[3]);
		if (controlfield[3].equals("0"))
			System.out.println(" Not ACK");
		else
			System.out.println(" Acknowlegment is valid");
		System.out.print("- Push : " + controlfield[4]);
		if (controlfield[4].equals("0"))
			System.out.println(" Not Push");
		else
			System.out.println(" Request for Push");
		System.out.print("- Reset : " + controlfield[5]);
		if (controlfield[5].equals("0"))
			System.out.println(" Not Reset");
		else
			System.out.println(" Reset the connection");
		System.out.print("- Syn : " + controlfield[6]);
		if (controlfield[6].equals("0"))
			System.out.println(" Not Syn");
		else
			System.out.println(" Synchronize sequence numbers");
		System.out.print("- Fin : " + controlfield[7]);
		if (controlfield[7].equals("0"))
			System.out.println(" Not Fin");
		else
			System.out.println(" Terminate the connection");

		l = l + 3;
		String TCP_WS = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Window Size : " + TCP_WS + " / " + Integer.parseInt(TCP_WS, 16));

		l = l + 4;
		String TCP_CS = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.println("Checksum : " + TCP_CS);

		l = l + 4;
		String TCP_UP = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.print("Urgent Port : " + TCP_UP);
		if (TCP_UP.equals("0000")) {
			System.out.println(" / not Urgent");
		} else {
			System.out.println(" / Urgent Pointer :" + TCP_UP);
		}

		l = l + 4;
		String TCP_O = "";
		for (int i = l; i < Ethernet.length; i++) {
			TCP_O += Ethernet[i];
		}
		System.out.println("Option : " + TCP_O + " / " + (TCP_O.length() / 2) + "bytes");
	}

	private static void ARP() {
		// TODO Auto-generated method stub
		System.out.println(" ARP\n--- ARP ---");

		String ARP_HWT = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.print("H/W Type : " + ARP_HWT);
		if (ARP_HWT.equals("0001")) {
			System.out.println(" / Ethernet");
		} else if (ARP_HWT.equals("0002")) {
			System.out.println(" / Experimental Ethernet");
		} else if (ARP_HWT.equals("0003")) {
			System.out.println(" / Amateur Radio AX.25");
		} else if (ARP_HWT.equals("0004")) {
			System.out.println(" / Proteon ProNet Token Ring");
		} else if (ARP_HWT.equals("0005")) {
			System.out.println(" / Chaos");
		} else if (ARP_HWT.equals("0006")) {
			System.out.println(" / IEEE 802.3 networks");
		} else if (ARP_HWT.equals("0007")) {
			System.out.println(" / ARCNEt");
		} else if (ARP_HWT.equals("0008")) {
			System.out.println(" / Hyperchnnel");
		} else if (ARP_HWT.equals("0009")) {
			System.out.println(" / Lanster");
		} else if (ARP_HWT.equals("000a")) {
			System.out.println(" / Autonet Short Address");
		} else if (ARP_HWT.equals("000b")) {
			System.out.println(" / LocalTalk");
		} else if (ARP_HWT.equals("000c")) {
			System.out.println(" / LocalNet(IBM PCNet or SYTEK LocalNET");
		}

		l = l + 4;
		String ARP_PT = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.print("Protocol type : " + ARP_PT);
		if (ARP_PT.equals("0800")) {
			System.out.println(" / IP");
		} else if (ARP_PT.equals("0805")) {
			System.out.println(" / X.25");
		}

		l = l + 4;
		String ARP_HWS = Ethernet[l] + Ethernet[l + 1];
		System.out.println("H/W Size : " + ARP_HWS + " / " + (Integer.parseInt(ARP_HWS, 16)) * 8 + " bits");

		l = l + 2;
		String ARP_PS = Ethernet[l] + Ethernet[l + 1];
		System.out.println("Protocol Size : " + ARP_PS + " / " + (Integer.parseInt(ARP_PS, 16)) * 8 + " bits");

		l = l + 2;
		String ARP_O = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3];
		System.out.print("Operation : " + ARP_O);
		if (ARP_O.equals("0001")) {
			System.out.println(" / ARP Request");
		} else if (ARP_O.equals("0002")) {
			System.out.println(" / ARP Reply");
		} else if (ARP_O.equals("0003")) {
			System.out.println(" / RARP Request");
		} else if (ARP_O.equals("0004")) {
			System.out.println(" / RARP Reply");
		}

		l = l + 4;
		String ARP_SMA = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7] + Ethernet[l + 8] + Ethernet[l + 9]
				+ Ethernet[l + 10] + Ethernet[l + 11];
		System.out.print("Sender Mac Address :");
		for (int i = l; i < l + ARP_SMA.length(); i++) {
			if (i == l) {
				System.out.print(Ethernet[i]);
			} else {
				if (i % 2 == 1) {
					System.out.print(Ethernet[i]);
				} else {
					System.out.print(":" + Ethernet[i]);
				}
			}
		}

		if (Integer.parseInt(Ethernet[l + 1], 16) % 2 == 0) {
			System.out.println(" / Unicast");
		} else if (Integer.parseInt(Ethernet[l + 1], 16) % 2 == 1) {
			System.out.println(" / Multicast");
		} else {
			System.out.println(" / Broadcast");
		}

		l = l + 12;
		String ARP_IPA = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7];
		System.out
				.println("Sender IP Address : " + ARP_IPA + " / " + Integer.parseInt(Ethernet[l] + Ethernet[l + 1], 16)
						+ "." + Integer.parseInt(Ethernet[l + 2] + Ethernet[l + 3], 16) + "."
						+ Integer.parseInt(Ethernet[l + 4] + Ethernet[l + 5], 16) + "."
						+ Integer.parseInt(Ethernet[l + 6] + Ethernet[l + 7], 16) + ".");

		l = l + 8;
		String ARP_TMA = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7] + Ethernet[l + 8] + Ethernet[l + 9]
				+ Ethernet[l + 10] + Ethernet[l + 11];
		System.out.print("Target Mac Address :");
		for (int i = l; i < l + ARP_TMA.length(); i++) {
			if (i == l) {
				System.out.print(Ethernet[i]);
			} else {
				if (i % 2 == 1) {
					System.out.print(Ethernet[i]);
				} else {
					System.out.print(":" + Ethernet[i]);
				}
			}
		}
		if (ARP_TMA.equals("000000000000")) {
			System.out.println(" / Unknown MAC");
		} else
			System.out.println(" / Target MAC Address : " + Integer.parseInt(Ethernet[l] + Ethernet[l + 1], 16) + "."
					+ Integer.parseInt(Ethernet[l + 2] + Ethernet[l + 3], 16) + "."
					+ Integer.parseInt(Ethernet[l + 4] + Ethernet[l + 5], 16) + "."
					+ Integer.parseInt(Ethernet[l + 6] + Ethernet[l + 7], 16));

		l = l + 12;
		String ARP_TIP = Ethernet[l] + Ethernet[l + 1] + Ethernet[l + 2] + Ethernet[l + 3] + Ethernet[l + 4]
				+ Ethernet[l + 5] + Ethernet[l + 6] + Ethernet[l + 7];
		System.out.println("Target IP Address : " + ARP_TIP + " / " + Integer.parseInt(Ethernet[l] + Ethernet[l + 1], 16)
						+ "." + Integer.parseInt(Ethernet[l + 2] + Ethernet[l + 3], 16) + "."
						+ Integer.parseInt(Ethernet[l + 4] + Ethernet[l + 5], 16) + "."
						+ Integer.parseInt(Ethernet[l + 6] + Ethernet[l + 7], 16));

	}

}
