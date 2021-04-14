package 고객관리시스템구현;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


/* 				외부클래스  (JFrame)				*/
public class CustomerSystem extends JFrame {	
	
	public static final String string = null;

	//내부 클래스 객체 생성 => 객체들을 전역으로 선언
	Menumain menumain = new Menumain();
	West west = new West();
	Showtable showtable = new Showtable();
	Buttons buttons = new Buttons();
	
	int updateRow;		// '수정' '이전', '다음' Button Mouse Event에서 사용
	int juminDoubleCheck; //주민등록번호 검사 여부 저장
	//외부클래스 생성자 
	public CustomerSystem() {
		
		OUTTER: while(true) {
			//이미지 jpg파일 인트로 화면으로 띄우기
			ImageIcon icon = new ImageIcon("images/intro.jpg");
			
			JOptionPane.showMessageDialog(null, null, "고객정보관리시스템", JOptionPane.NO_OPTION, icon);
			
			//패스워드 인증창 띄우기
			String password = JOptionPane.showInputDialog("고객관리시스템" + "\n" + "패스워드입력");
			//String password = "1111";
			String passwd = "1111";
			
			if(password.equals(passwd)) {
				setTitle("고객관리시스템");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//종료버튼시 JFrame 스레드 종료시킴 (없을시 종료버튼눌러도 스레드 동작유지가됨)
				
				//Build GUI >>>   JFrame 'BorderLayout'에 Panel Component 빌드하기 
				add(menumain.mb, BorderLayout.NORTH);
				add(west, BorderLayout.WEST);
				add(buttons, BorderLayout.SOUTH);
				add(showtable.scroll, BorderLayout.CENTER);
				
				setSize(1000, 800);
				setLocation(0, 0);
				setVisible(true);
				
				break OUTTER;
			} else {
				JOptionPane.showMessageDialog(null, "패스워드가 맞지 않습니다 \n '확인' 버튼을 누르세요", "패스워드 인증실패", JOptionPane.ERROR_MESSAGE);
				continue OUTTER;
			}
		}
	}
	
	/* 				 내부클래스 (메뉴)				*/
	class Menumain extends JPanel implements ItemListener {
		JMenuBar 			mb;
		JMenu 				file, sort, help;
		JMenuItem			fopen, fsave, exit, proinfo;
		JCheckBoxMenuItem	snum, sname, slocation, sjob;
		
		FileDialog			readOpen, saveOpen;
		String 				readFileName, saveFileName;
		
		
		public Menumain() {
			//MenuBar (Menu의 최상위 Component)
			mb = new JMenuBar();			
			//Menu title
			file = new JMenu("파일");		
			sort = new JMenu("정렬");
			help = new JMenu("도움말");
			//Menu item
			fopen = new JMenuItem("열기");
			fsave = new JMenuItem("저장");
			exit = new JMenuItem("닫기");
			//정렬 CheckBox menu item
			snum = new JCheckBoxMenuItem("번호");
			sname = new JCheckBoxMenuItem("이름");
			slocation = new JCheckBoxMenuItem("출생지역");
			sjob = new JCheckBoxMenuItem("직업");
			//도움말 menu item
			proinfo = new JMenuItem("프로그램정보");
			//메뉴에 하위 menu item 추가
			file.add(fopen); file.add(fsave);  file.addSeparator(); file.add(exit);
			sort.add(snum);	sort.add(sname); sort.add(slocation); sort.add(sjob); 
			help.add(proinfo);
			//MenuBar에 Menu title 추가
			mb.add(file); mb.add(sort); mb.add(help);
		
			//////////////   파일 열기 이벤트   ////////////////////////
			fopen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//StringTokenizer Class 사용 
					StringTokenizer st;
					Vector<String> vec;
					
					readOpen = new FileDialog(CustomerSystem.this, "파일열기", FileDialog.LOAD);  //CustomerSystem.this -> Parent JFrame
					readOpen.setVisible(true);
					
					readFileName =  readOpen.getDirectory() + "\\" + readOpen.getFile();	//선택파일경로
					
					//IOException(FileNotFoundException 상위)
					try {
						//BufferedReader 처리(문자열 line단위 처리 및 버퍼를 통한 속도 목적)
						BufferedReader br = new BufferedReader(new FileReader(readFileName));
						showtable.data.removeAllElements(); //data 벡터 초기화
						String line = "";
						while((line = br.readLine()) != null) {
							//StringTokenizer, Vector는 사용할때마다 객체 생성
							st = new StringTokenizer(line, ", ");
							//Vector 초기화
							vec = new Vector<String>();
							while(st.hasMoreTokens()) {		// Token이 있을때 까지 
								vec.add(st.nextToken()); 	//벡터에 추가
							}
							
							//showtable 클래스 2차원Vector변수 data에 vector값 추가
							showtable.data.addElement(vec);
						}
						//변경사항을 해당 Listener 에 변경사항 전달
						showtable.datamodel.fireTableDataChanged();
						br.close();
					} catch(IOException ex) {
						System.out.println(ex);
					}
				}
			});
			//////////////   파일 저장 이벤트   ////////////////////////
			fsave.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// Frame parent -> JFrame 객체를 상속한 부모클래스 지정
					saveOpen = new FileDialog(CustomerSystem.this, "파일열기", FileDialog.SAVE);
					saveOpen.setVisible(true);
					
					saveFileName = saveOpen.getDirectory() + "//" + saveOpen.getFile();
					//System.out.println(saveFileName);
					
					String str = "";  // 전체행 변수
					String temp = ""; // 한행 
					
					//IOException, FileNotFoundException 를 위한 예외처리 필수
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(saveFileName));
						
						for(int i = 0; i < showtable.table.getRowCount(); i++) {	//table의 행(row)의 갯수만큼 반복처리
							//2차원 배열의 벡터변수 data에서 데이타를 가져오는 매서드 => elementAt
							temp = showtable.data.elementAt(i).toString();
							//[name, hp, juminnumber] -> 앞뒤 [] 제거후 저장 처리    &  개행처리
							str += temp.substring(1, temp.length()-1) + "\n";	
						}
						bw.write(str);
						bw.close();
					} catch(Exception ex) {
						System.out.println(ex);
					}
				}
			});
			//////////////   메뉴 종료 이벤트   ////////////////////////
			exit.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			// 정렬 버튼 이벤트 연결
			snum.addItemListener(this);
			sname.addItemListener(this);
			slocation.addItemListener(this);
			sjob.addItemListener(this);
			
			//도움말 
			proinfo.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new newWindow();	// 클래스 newWindow 
				}
			});
		}
		//////////////   정렬 이벤트   ////////////////////////
		@Override
		public void itemStateChanged(ItemEvent e) {
			
			int index = 0;
			if(e.getSource().equals(snum)) 		index = 0;   
			if(e.getSource().equals(sname)) 	index = 1;	
			if(e.getSource().equals(slocation)) index = 7;	
			if(e.getSource().equals(sjob)) 		index = 9;	
			
			int sortIndex = index;
			
			int row = showtable.table.getRowCount();	// 행의 갯수
			int col = showtable.table.getColumnCount(); // 열의 갯수
			
			String[][] arr = new String[row][col];		// JTable row,col값으로 배열초기화   
			
			//JTable의 데이터 전체를  2차원 배열로 담기
			for(int i = 0; i < row; i++) {
				for(int j = 0; j < col; j++) {
					arr[i][j] = (String) showtable.table.getValueAt(i, j);
				}
			}
			
			//정렬방식 - Arrays sort()매서드 lambda 람다식에서는 final 변수만가능(변수값 변경된 변수는 불가능)
			if (sortIndex == 0)   Arrays.sort(arr, (a, b) -> Integer.compare(Integer.parseInt(a[sortIndex]), Integer.parseInt(b[sortIndex])));
			else Arrays.sort(arr, (a, b) -> a[sortIndex].compareTo(b[sortIndex])); 
			
			//Jtable에 정렬된 데이타 출력
			for(int i = 0; i < row; i++) {
				for(int j = 0; j < col; j++) {
					showtable.table.setValueAt(arr[i][j], i, j);
				}
			}
			//체크박스 선택해제
			snum.setSelected(false);
			sname.setSelected(false);
			slocation.setSelected(false);
			sjob.setSelected(false);
		}
	}

	/*			West 패널배치	 (내부클래스) 			*/ 
	class West extends JPanel {

		Input input = new Input();		//입력클래스
		Output output = new Output();	//출력클래스
		
		public West() {
			setLayout(new BorderLayout());		//레이아웃설정
			add(input, BorderLayout.CENTER);	//입력패널위치추가
			add(output, BorderLayout.SOUTH);	//출력패널위치
		}	
	}
	/*			입력 패널배치	 (내부클래스) 			*/
	class Input extends JPanel {
		JLabel					la, la2;
		JTextField				tf[];	// JTextField 컴포넌트 객체들의 주소값을 저장할 배열
		JComboBox<String>		cb;
		JButton					juminBtn;
		
		public Input() {
			//라인 보더 만들기
			LineBorder line = new LineBorder(Color.ORANGE, 2);
			setBorder(new TitledBorder(line, "입력"));
			
			String[] text = {"번호", "이름", "핸드폰번호", "이메일", "주민등록번호"};
			tf = new JTextField[5];
			juminBtn = new JButton("중복검사");
			juminBtn.setPreferredSize(new Dimension(10,10));
			//GridLayout 배치  
			setLayout(new GridLayout(6, 3, 10, 30)); //  GridLayout(int rows, int cols, int hgap, int vgap)
			for(int i = 0; i < text.length; i++) {
				la = new JLabel(text[i]);
				tf[i] = new JTextField();		
				la.setHorizontalAlignment(JLabel.CENTER);
				add(la);  add(tf[i]);   
				if(i == text.length-1)	add(juminBtn); 
				else 					add(new JLabel(""));	
			}
			
			la2 = new JLabel("직업");
			la2.setHorizontalAlignment(JLabel.CENTER);
			String[] cbText = {"무직", "회사원", "학생"};
			cb = new JComboBox<String>(cbText);
			add(la2); add(cb); add(new JLabel());
			
			//최상위 컨테이너인 JFrame 말고 부분적인 사이즈를 설정할때는 Dimension 사용
			setPreferredSize(new Dimension(360, 200));
			setBackground(Color.WHITE);
			
			///  중복검사 버튼 이벤트  /////
			juminBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int juminCheck = 1;	//값체크용
					
//					String[] chkArr = {"번호", "이름", "핸드폰번호", "이메일", "주민등록번호"};
					
//					for(int i = 0; i < showtable.data.size(); i++) {
//						for (int j = 0; j < chkArr.length; j++) {
//							if(showtable.data.elementAt(i).get(j).contains(tf[j].getText().trim())) {
//								JOptionPane.showMessageDialog(null, "등록된 " + chkArr[j] + "(이)가 이미 존재합니다", "중복 오류 Message", JOptionPane.ERROR_MESSAGE);
//								juminCheck = 0;
//								west.input.tf[j].setText(null);
//								west.input.tf[j].requestFocus();
//								return;
//							}
//						}
//					}
					
					
					for(int i = 0; i < showtable.data.size(); i++) {
						if(showtable.data.elementAt(i).get(4).contains(tf[4].getText().trim())) {	
							JOptionPane.showMessageDialog(null, "등록된 주민등록번호가 이미 존재합니다", "주민번호 중복오류", JOptionPane.ERROR_MESSAGE);
							juminCheck = 0;
							west.input.tf[4].setText(null);
							west.input.tf[4].requestFocus();
							return;
						}
					}
					if(juminCheck == 1) {
						JOptionPane.showMessageDialog(null, "중복된 고객이 없습니다!", "중복 체크 메시지", JOptionPane.INFORMATION_MESSAGE);
						west.input.tf[4].setEnabled(false);
						west.input.juminBtn.setEnabled(false);
						juminDoubleCheck = 1;	//전역변수 초기화 (추가버튼 이벤트시 중복검사진행 여부 체크용)
					}
				}
			});
			
		}
	}
	/*			출력 패널 과 정보검색 CardLayout 배치	 (내부클래스) 			*/
	class Output extends JPanel {
		JLabel lbLeft;	//신상정보 좌측 label
		JLabel lbRight[];	//신상정보 우측 label
		
		JPanel info = new JPanel();	  // '신상정보'카드를 만들기 위한 패널
		JPanel search = new JPanel(); // '정보검색'카드를 만들기 위한 패널	
		CardLayout card;
		JRadioButton searchRadio[] = new JRadioButton[4];
		JTextField findText = new JTextField(10);
		JButton searchBtn, exitBtn;
		
		
		public Output() {
			// 카드레이아웃 설정
			card = new CardLayout();
			setLayout(card);
			
			////// 신상정보 카드 레이아웃
			info.setBackground(Color.WHITE);
			info.setBorder(new TitledBorder(new LineBorder(Color.ORANGE, 2), "신상정보"));
			//GridLayout 배치  
			info.setLayout(new GridLayout(4, 2, 10, 5));
			
			String[] text = {"나   이", "성    별", "출생지역", "생    일"};
			lbRight = new JLabel[4];
			for(int i = 0; i < text.length; i++) {
				lbLeft = new JLabel(text[i]);
				lbRight[i] = new JLabel();
				info.add(lbLeft);
				info.add(lbRight[i]);
			}
		
			info.setPreferredSize(new Dimension(360, 300));
			add(info,"첫번째카드");
			
			///// 정보검색 카드 레이아웃
			search.setBackground(Color.ORANGE);
			search.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2), "정보검색"));
			
			ButtonGroup group = new ButtonGroup();
			String search_name[] = {"이름", "직업", "출생지역", "생년월일"};
			
			searchBtn = new JButton("찾   기");
			searchBtn.setBackground(Color.GREEN);
			exitBtn = new JButton("나가기");
			exitBtn.setBackground(Color.GRAY);
			
			//자유배치
			search.setLayout(null);
			int x = -70;
			search.setPreferredSize(new Dimension(360,200));
			
			for(int i = 0; i < searchRadio.length; i++) {
				searchRadio[i] = new JRadioButton(search_name[i]);
				searchRadio[i].setBounds(x += 80, 30, 80, 30);  // (int x, int y, int width, int height)
				searchRadio[i].setBackground(Color.ORANGE);

				group.add(searchRadio[i]);
				search.add(searchRadio[i]);
			}
			
			findText.setBounds(25, 80, 200, 30);
			searchBtn.setBounds(25, 130, 70, 30);
			exitBtn.setBounds(115, 130, 110, 30);
			search.add(findText);
			search.add(searchBtn);
			search.add(exitBtn);

			add(search, "두번째카드");
			
			
			/////  나가기 이벤트 연결   ////////////////////////
			exitBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//JTable에 원래정보 출력 
					showtable.datamodel.setDataVector(showtable.data, showtable.columnNames); 
					//검색버튼활성화 
					buttons.searchBtn.setEnabled(true); 
					//카드레이아웃변경 
					west.output.card.next(west.output); 
					findText.setText(null);
					showtable.setTable();
					if(west.input.tf[0].getText().isEmpty()) {
						west.output.lbRight[0].setText(null);
						west.output.lbRight[1].setText(null);
						west.output.lbRight[2].setText(null);
						west.output.lbRight[3].setText(null);
					}
				}
			});
			
			//찾기 이벤트 연결
			searchBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int flag = 1;
					// 라디오체크박스 선택체크
					for(int i=0; i < searchRadio.length; i++) {
						if(searchRadio[i].isSelected()) {
							flag = 0;
							break;
						}
					}
					
					if(flag > 0) {
						JOptionPane.showMessageDialog(null, "검색 기준을 선택하세요", "경고메시지", JOptionPane.ERROR_MESSAGE);
					} else if(findText.getText().length() == 0) {
						JOptionPane.showMessageDialog(null, "검색란에 입력해주세요", "경고메시지", JOptionPane.ERROR_MESSAGE);
					} else {
						//JTable에서 검색된 2차배열을 저장하여 뿌려주기 위해 임시벡터 사용
						Vector<Vector<String>> findData = new Vector<Vector<String>>();
						
						// 선택한 라디오박스로 JTable의 검색할 열의 인덱스값 초기화
						int num = 0;
						if(searchRadio[0].isSelected()) num = 1;	//이름
						if(searchRadio[1].isSelected()) num = 9;	//직업
						if(searchRadio[2].isSelected()) num = 7;	//출생지역
						if(searchRadio[3].isSelected()) num = 8;	//생일
						
						for(int i = 0; i < showtable.data.size(); i++) {
							//2차원 Vector인 data에서 행중 해당 열중 입력값이 포함되어 있는지 체크한다 
							if(showtable.data.elementAt(i).get(num).contains(findText.getText().trim())) {
								//임시 2차원 백터에 추가
								findData.addElement(showtable.data.elementAt(i));
							}
						}
						//벡터에 아무것도 없다면 (검색된 값이 없을경우)
						if(findData.isEmpty()) {
							JOptionPane.showMessageDialog(null, "검색하신 내용이 없습니다", "경고메시지", JOptionPane.ERROR_MESSAGE);
							//showtable.datamodel.setDataVector(showtable.data, showtable.columnNames);
							findText.setText(null);
						} else {
						// JTable datamodel에 2차원 백터넣기
						// 컬럼 벡터와 데이터 벡터 합치기
							showtable.datamodel.setDataVector(findData, showtable.columnNames);
							showtable.datamodel.fireTableDataChanged();
							showtable.setTable();
						}
						
					}
				}
			});
			
			
		}
	}

	/*			JTable 출력	 (내부클래스) 			 
	내부클래스 () Mouse인터페이스인 Listner를 사용시 모든 메소드를 다 구현해야 하지만 
	하나의 메소드만 필요하므로 Adapter Class를 상속하였다 						 */
	class Showtable {
		
		DefaultTableModel		datamodel;
		JTable					table;
		JScrollPane				scroll;
		
		String[] colName = {"번호", "이름", "핸드폰번호", "E-Mail", "주민등록번호", 
							"나이", "성별", "출생지역", "생일", "직업"};
		
		//[중요] JTable에 있는 2차원 데이타들을 처리하기 위한 2차원 Vector사용  
		Vector<Vector<String>> 	data;
		Vector<String>	 columnNames;

		public Showtable() {
			data = new Vector<Vector<String>>();  // 기본10개의 가변배열 객체생성
			columnNames = new Vector<String>();
			
			//DefaultTableModel생성을 위한 vector타입으로 변환
			for(int i = 0; i < colName.length; i++) {
				columnNames.add(colName[i]);
			}
			
			//JTable에 자료를 넣을때 Object형 TableModel, Vector를 파라미터값으로 넣음
			//데이터 삽입,수정,삭제를 위해 Model형태를 사용해야 함 (다른 타입은 출력만 가능)
			//DefaultTableModel(Vector data, Vector ColumnNames) 생성자사용
			datamodel = new DefaultTableModel(data, columnNames);
			//2단계 table에 넣기
			table = new JTable(datamodel);
			//3단계 스크롤패널 붙이기
			scroll = new JScrollPane(table);
			
			//각 셀의 크기 조절,  셀 데이타 정렬,  컬럼버튼 정렬
			setTable();
			scroll.getViewport().setBackground(Color.WHITE);

			//이벤트 연결
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateRow = table.getSelectedRow();
					//선택된열의 행값을 가져와서 입력TextField에 넣기
					for(int i = 0; i < table.getColumnCount(); i++) {
						if(i < 5) 				west.input.tf[i].setText((String)table.getValueAt(updateRow, i));
						if(i >= 5 && i <= 8 )   west.output.lbRight[i-5].setText((String) table.getValueAt(updateRow, i));
						if(i == 9) 				west.input.cb.setSelectedItem((String)table.getValueAt(updateRow, i));	 
					}
					
					// 비활성화
					west.input.tf[4].setEnabled(false);	// 주민번호입력
					west.input.juminBtn.setEnabled(false);	// 주민번호 중복검사
					buttons.addBtn.setEnabled(false);	// 추가버튼
				}
			});
		
			
						
		}
		///////   테이블 정렬 및 컬럼별 Sort기능
		private void setTable() {
			table.getColumnModel().getColumn(0).setPreferredWidth(30);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(30);
			table.getColumnModel().getColumn(6).setPreferredWidth(30);
			table.getColumnModel().getColumn(7).setPreferredWidth(70);
			table.getColumnModel().getColumn(8).setPreferredWidth(70);
			table.getColumnModel().getColumn(9).setPreferredWidth(50);
			
			// 셀의 데이타를 가운데 정렬시키기
			DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
			cell.setHorizontalAlignment(SwingConstants.CENTER);
			
			for(int i=0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(cell);
			}
			
			//컬럼별버튼 누르면  정렬하기 
//			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel()); 
//			table.setRowSorter(sorter); 
//			List<RowSorter.SortKey> sortKeys = new ArrayList<>(10); 
//			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); //SortKey(int column, SortOrder sortOrder) 
//			sorter.setSortKeys(sortKeys);
		}
	}
	
	/*			JTable 출력	 (내부클래스) 		*/
	class Buttons extends JPanel {
		Vector<String> vector;
		JButton addBtn, updateBtn, delBtn, lastBtn, nextBtn, searchBtn, initBtn;
		String juminNo;
		
		public Buttons() {
			//FlowLayout 배치 
			//setLayout(new FlowLayout(3, 10, 10));
			setLayout(new GridLayout(1, 6));
			//버튼생성
			addBtn = new JButton("추가");	 			delBtn = new JButton("삭제");
			lastBtn = new JButton("이전");				nextBtn = new JButton("다음");
			updateBtn = new JButton("수정");				searchBtn = new JButton("검색");
			initBtn = new JButton("초기화");
			
			//버튼배경색상
			addBtn.setBackground(Color.ORANGE);			delBtn.setBackground(Color.GRAY);
			lastBtn.setBackground(Color.CYAN);			nextBtn.setBackground(Color.CYAN);
			updateBtn.setBackground(Color.PINK);		searchBtn.setBackground(Color.GREEN);
			
			// JPanel에 버튼 추가
			add(addBtn);	add(delBtn);	add(lastBtn);	add(nextBtn);	add(updateBtn);		add(searchBtn);	add(initBtn);

			//////        추가버튼 이벤트 구현          ////////////
			addBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					vector = new Vector<String>();
					
					//입력 체크 유무 사용자 메서드 
					boolean checkInput = validCheckInput();
					if(checkInput == false) return;
					
					//핸드폰번호 입력값 검사
					String hpNum = west.input.tf[2].getText();		//핸드폰번호필드값
					String hpnum_pattern = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$";
					boolean checkHP = hpNum.matches(hpnum_pattern);
					if(checkHP == false) {
						JOptionPane.showMessageDialog(null, "핸드폰 입력을 다시 확인해주세요", "핸드폰 입력오류", JOptionPane.ERROR_MESSAGE);
						west.input.tf[2].requestFocus();
						return;
					}
					//이메일 입력값 검사
					String email = west.input.tf[3].getText();		//이메일필드값
					String email_pattern = "^\\w+(\\.)?\\w+@\\w+\\.\\w+(\\.\\w+)?";
					boolean checkEmail = email.matches(email_pattern);
					if(checkEmail == false) {
						JOptionPane.showMessageDialog(null, "이메일 입력을 다시 확인해주세요", "이메일 입력오류", JOptionPane.ERROR_MESSAGE);
						west.input.tf[3].requestFocus();
						return;
					}
					
					if(juminDoubleCheck == 0) {
						JOptionPane.showMessageDialog(null, "등록전 주민등록 중복검사를 먼저 진행해주세요", "주민등록번호 중복검사 요청", JOptionPane.ERROR_MESSAGE);
						return;
					}
						
					
					// 주민번호 검사 및 나이, 성별, 출생지역, 생일 추출 
					String juminNum = west.input.tf[4].getText();	//주민번호필드값
					/**
					 * 입력된 주민번호 유효성 검사
					 * 입력값 검사  (정규표현식패턴) ^[0-9]{6}-[1-4][0-9]{6}$ 시작: ^ 끝:$
					 */
					String regex = "^[0-9]{6}-[1-4][0-9]{6}$";
					Boolean check = juminNum.matches(regex);
						
					if(check == false) {
						JOptionPane.showMessageDialog(null, "주민번호가 올바르지 않습니다", "주민번호 입력 오류", JOptionPane.ERROR_MESSAGE);
						west.input.tf[4].setText(null);
						west.input.tf[4].requestFocus();
						return;	//그 상태 유지
					}
					// 주민번호 유효성체크
					Boolean validJumin = calJuminCode(juminNum);
					if(validJumin == false) return;
					
					// 나이계산
					String age = calAge(juminNum);
					// 성별체크
					String gender="";
					if (juminNum.charAt(7)-'0' == 1 || juminNum.charAt(7)-'0' == 3) gender = "남자";
					else gender = "여자";
					// 출생지역계산
					String location = getLocation(juminNum);
					// 생일계산
					String birthday = getBirthDay(juminNum);
					
					//신상정보 출력용 배열
					String[] info = {age, gender, location, birthday};
					//신상정보 Label에 출력
					for(int i = 0; i < info.length; i++) 
						west.output.lbRight[i].setText(info[i]);
					//west에 있는 입력필드에서 값 가져와서 벡터배열에 추가
					for(int j = 0; j < showtable.table.getColumnCount(); j++) {
						if(j <= 4)			vector.add(west.input.tf[j].getText()); 
						if(j >= 5 && j < 9)  vector.add(info[j-5]); 
						if(j == 9) 			vector.add((String) west.input.cb.getSelectedItem());	// 직업
					}
					//showtable클래스에 있는 2차원 Vector<Vector<String>> data에 추가 
					showtable.data.addElement(vector);
					
					//중요 fileTableDataChanged()메소드를 호출하면
					//JTable의 데이터가 변경되었음을 리스너에게 보낸다. 
					showtable.datamodel.fireTableDataChanged();
					
					initTextField(); //입력필드초기화 매서드 호출
					juminDoubleCheck = 0;
					JOptionPane.showMessageDialog(null, "고객등록이 정상적으로 되었습니다.", "고객등록완료", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			/////////////////    수정버튼 이벤트 구현   //////////////////
			updateBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// JTable의 선택된 row 개수 반환 
					updateRow = showtable.table.getSelectedRow(); 
					//System.out.println("updateRow" + updateRow); 
					if(updateRow == -1) { 
						JOptionPane.showMessageDialog(null, "수정할 회원정보가 선택되지 않았습니다", "수정 선택열 오류", JOptionPane.ERROR_MESSAGE);		 
						return; 
					} 
					//입력 체크 유무 사용자 메서드 
					boolean checkInput = validCheckInput();
					if(checkInput == false) 	return;
					
					// JTable의 데이터를 변경해주는 Method
					//-> setValueAt(data, row, column);
					showtable.table.setValueAt(west.input.tf[0].getText(), updateRow, 0);
					showtable.table.setValueAt(west.input.tf[1].getText(), updateRow, 1);
					showtable.table.setValueAt(west.input.tf[2].getText(), updateRow, 2);
					showtable.table.setValueAt(west.input.tf[3].getText(), updateRow, 3);
					showtable.table.setValueAt(west.input.tf[4].getText(), updateRow, 4);
					showtable.table.setValueAt(west.input.cb.getSelectedItem(), updateRow, 9);
					
					//주민번호는 수정안함
					//west.input.tf[4].setEnabled(true);  //주민번호 입력 허용
					showtable.table.removeRowSelectionInterval(0, updateRow); // 테이블 선택값제거
					initTextField();	// 입력필드 초기화
					JOptionPane.showMessageDialog(null, "고객등록수정이 정상적으로 처리 되었습니다.", "고객수정완료", JOptionPane.INFORMATION_MESSAGE);

				}
			});
			/////////////////    삭제버튼 이벤트 구현   //////////////////
			delBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int deleteRow = showtable.table.getSelectedRow();
					
					if(deleteRow == -1) {	
						JOptionPane.showMessageDialog(null, "삭제할 회원정보가 선택되지 않았습니다", "삭제 선택열 오류", JOptionPane.ERROR_MESSAGE);		
						return;
					}
					int select = JOptionPane.showConfirmDialog(null, "선택한 회원을 정말 삭제하시겠습니까?", "삭제 확인 메시지", JOptionPane.YES_NO_OPTION);
					if(select == JOptionPane.YES_OPTION) {
						//테이블의 모델을 가져오는 메소드
						//showtable.datamodel = showtable.table.getModel();
						//해당하는 위치의 열을 삭제
						showtable.datamodel.removeRow(deleteRow);
						initTextField();
						JOptionPane.showMessageDialog(null, "고객정보 삭제가 정상적으로 되었습니다.", "고객정보 삭제완료", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
			});
			//////////  검색 버튼 이벤트 //////////////
			searchBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					west.output.card.next(west.output);
					searchBtn.setEnabled(false);
					initTextField();
					west.output.searchRadio[0].setSelected(true);
					
				}
			});
			//////      이전버튼 이벤트 구현          ////////////
			lastBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRow = updateRow - 1;	//선택된열의 이전값
					
					//System.out.println("이전버튼이벤트 현재 출력할 Row값 : " + updateRow);
					if(updateRow < 0) {
						JOptionPane.showMessageDialog(null, "더이상 출력할 데이타가 없습니다", "출력 없음 메시지", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					for(int i = 0; i < showtable.table.getColumnCount(); i++) {
						if(i < 5) 				west.input.tf[i].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i >= 5 && i <= 8 )   west.output.lbRight[i-5].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i == 9) 				west.input.cb.setSelectedItem((String)showtable.table.getValueAt(updateRow, i));	 
					}
					//주민번호 수정 비활성화
					west.input.tf[4].setEnabled(false);
				}
			});
			//////      다음버튼 이벤트 구현          ////////////
			nextBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRow = updateRow + 1;	//선택된행의 다음값
					int lastRow = showtable.table.getRowCount() - 1; //마지막 행값
					//System.out.println("이전버튼이벤트 현재 출력할 Row값 : " + updateRow);
					if(updateRow > lastRow) {
						JOptionPane.showMessageDialog(null, "더이상 출력할 데이타가 없습니다", "출력 없음 메시지", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					for(int i = 0; i < showtable.table.getColumnCount(); i++) {
						if(i < 5) 				west.input.tf[i].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i >= 5 && i <= 8 )   west.output.lbRight[i-5].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i == 9) 				west.input.cb.setSelectedItem((String)showtable.table.getValueAt(updateRow, i));	 
					}
					//주민번호 수정 비활성화
					west.input.tf[4].setEnabled(false);
				}
			});
			//////  초기화 버튼 이벤트 구현          ////////////
			initBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					initTextField();
					//west.input.juminBtn.setEnabled(true);
					west.output.lbRight[0].setText(null);
					west.output.lbRight[1].setText(null);
					west.output.lbRight[2].setText(null);
					west.output.lbRight[3].setText(null);
					buttons.addBtn.setEnabled(true);
					if(showtable.table.getSelectedRow() >= 0) {
						showtable.table.removeRowSelectionInterval(0, updateRow); // 테이블 선택값제거
					}
				}
			});
			
		}
		
		
		////// 입력값 체크 매서드 ////////
		private boolean validCheckInput() {
			if(west.input.tf[0].getText().trim().isEmpty()) { 
				JOptionPane.showMessageDialog(null, "번호가 입력이 안되었습니다", "입력 오류", JOptionPane.ERROR_MESSAGE);
				west.input.tf[0].requestFocus();
				return false;
			}
			if(west.input.tf[1].getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "이름이 입력이 안되었습니다", "입력 오류", JOptionPane.ERROR_MESSAGE);
				west.input.tf[1].requestFocus();
				return false;
			}
			if(west.input.tf[2].getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "핸드폰번호가 입력이 안되었습니다", "입력 오류", JOptionPane.ERROR_MESSAGE);
				west.input.tf[2].requestFocus();
				return false;
			}
			//핸드폰번호 입력값 검사
			String hpNum = west.input.tf[2].getText();		//핸드폰번호필드값
			String hpnum_pattern = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$";
			boolean checkHP = hpNum.matches(hpnum_pattern);
			if(checkHP == false) {
				JOptionPane.showMessageDialog(null, "핸드폰 입력을 다시 확인해주세요", "핸드폰 입력오류", JOptionPane.ERROR_MESSAGE);
				west.input.tf[2].requestFocus();
				return false;
			}
			//이메일 입력값 검사
			String email = west.input.tf[3].getText();		//이메일필드값
			String email_pattern = "^\\w+(\\.)?\\w+@\\w+\\.\\w+(\\.\\w+)?";
			boolean checkEmail = email.matches(email_pattern);
			if(checkEmail == false) {
				JOptionPane.showMessageDialog(null, "이메일 입력을 다시 확인해주세요", "이메일 입력오류", JOptionPane.ERROR_MESSAGE);
				west.input.tf[3].requestFocus();
				return false;
			}
			return true;
		}
		////// 입력값 초기화 매서드 ////////
		public void initTextField() {
			//입력필드 비우고 포커스이동
			west.input.tf[0].setText(null);
			west.input.tf[1].setText(null);
			west.input.tf[2].setText(null);
			west.input.tf[3].setText(null);
			west.input.tf[4].setText(null);
			west.input.juminBtn.setEnabled(true);
			west.input.cb.setSelectedIndex(0);
			west.input.tf[0].requestFocus();
			west.input.tf[4].setEnabled(true);
			buttons.addBtn.setEnabled(true);
		}
		////// 나이계산 매서드 ////////////////
		public String calAge(String juminNum) {
			/**
			 * 1.현재년도를 시스템으로부터 얻어내기 Calender 클래스 적용 2. 주민번호 앞자리의 생년월일에서 두자리만 가져오기"97" -> 정수로
			 * 변환 String클래스의 substring()메소드 적용 3. 뒷7자리중 첫번째 자리 1,2,3,4 중 어느것인지 비교판단하여 if 1,2
			 * 1900을 더해주고 if 3,4 -> 2000을 더해준다 4. (2021-1997)+1 = 나이
			 */
			Calendar cal = Calendar.getInstance(Locale.KOREA);
			int nowYear = cal.get(Calendar.YEAR); // 시스템 연도
			int myYear = Integer.parseInt(juminNum.substring(0, 2)); // 내 주민번호 연도

			int genderCode = (juminNum.charAt(7) - '0'); // 내 성별자리
			if (genderCode == 1 || genderCode == 2)
				myYear += 1900;
			if (genderCode == 3 || genderCode == 4)
				myYear += 2000;
			int age = (nowYear - myYear) + 1;
			return String.valueOf(age);
		}
		////// 주민번호 유효성 체크 매서드  ////////////
		public Boolean calJuminCode(String juminNum) {
			// 주민번호 유효성 체크
			int[] weight = { 2, 3, 4, 5, 6, 7, 0, 8, 9, 2, 3, 4, 5 }; // 국가검증 가중치값
			int sum = 0;
			int[] code = new int[2];
			for (int i = 0; i < 13; i++) {
				if (juminNum.charAt(i) != '-') {
					sum += (juminNum.charAt(i) - '0') * weight[i];
				}
			}
			int sysCode = 11 - (sum % 11);
			code[0] = sysCode % 10;
			code[1] = juminNum.charAt(13) - '0'; // 주민번호 마지막 자리수
			
			if(code[0] != code[1]) {
				JOptionPane.showMessageDialog(null, "유효한 주민등록번호가 입력되지 않았습니다", "주민번호 유효성검사 오류", JOptionPane.ERROR_MESSAGE);
				west.input.tf[4].setEnabled(true);
				west.input.juminBtn.setEnabled(true);
				west.input.tf[4].requestFocus();
				juminDoubleCheck = 0;
				return false;	
			}
			return true;
		}
		//// 출생등록지 추출 매서드   //////////
		public String getLocation(String juminNum) {
			/*
			 * 출생등록지  -> 주민번호 뒤자리중 2~3번째 자리 비교 출력 
			 */
			int myLocationCode = Integer.parseInt(juminNum.substring(8, 10));
			String[][] arrLocation = {{"서울","00","08"}, {"부산","09","12"},{"인천","13","15"},{"경기","16","25"},
						{"강원","26","34"}, {"충북","35","39"},{"대전","40","40"},{"충남","41","43"},
						{"충남","45","47"}, {"세종","44","44"},{"세종","96","96"},{"전북","48","54"},
						{"전남","55","64"}, {"광주","65","66"},{"대구","67","70"},{"경북","71","80"},
						{"경남","81","84"}, {"경남","86","90"},{"울산","85","85"},{"제주","91","95"}};
			String location = null;
			for(int i=0; i < arrLocation.length; i++) {
				int start = Integer.parseInt(arrLocation[i][1]); 
				int end = Integer.parseInt(arrLocation[i][2]);
				if(myLocationCode >= start && myLocationCode <= end)	location = arrLocation[i][0]; 
			}
			return location;
		}
		/////  생년월일 계산 매서드 ////////
		public String getBirthDay(String juminNum) {
			int myYear = Integer.parseInt(juminNum.substring(0, 2)); // 내 주민번호 연도
			int genderCode = (juminNum.charAt(7) - '0'); 
			if (genderCode == 1 || genderCode == 2)	myYear += 1900;
			if (genderCode == 3 || genderCode == 4)	myYear += 2000;
			String birthday = String.valueOf(myYear) + juminNum.substring(2, 6);
			return birthday;
		}
	}
	
	//도움말 프로그램정보 새창
	class newWindow extends JFrame { 
	
		JTextArea ta = new JTextArea(7, 20);
		
		public newWindow() {
			// 주의, 여기서 setDefaultCloseOperation() 정의를 하지 말아야 한다
	        // 정의하게 되면 새 창을 닫으면 모든 창과 프로그램이 동시에 꺼진다
	        setTitle("프로그램정보");
	        Container c = getContentPane();
			c.setLayout(new FlowLayout());
	        String str = "고객관리 시스템 \n \n" + 
	        		"주요기능 : 고객정보 추가 및 수정, 삭제 \n" +
	        		"         파일열기 및 저장  \n"+ 
	        		"         검색 및 정렬 기능 \n \n" +
	        		"  <<계정이력(Modificatoin Information)>>  \n\n" + 
	        		"  수정일           수정자		수정내용\r\n" + 
	        		"  ___________________________________________\r\n" + 
	        		"   2021.04.08  최성은     화면구성, 고객정보 추가,수정,삭제이벤트처리\r\n" + 
	        		"   2021.04.09  최성은     파일열기 및 저장, 신상정보 출력 \r\n" + 
	        		"   2021.04.10  최성은     고객정보 검색 및 정렬기능";
	        
	        ta.setText(str);
	        c.add(ta);
	        
	        setSize(400,300);
	        setLocation(500,300);
	        setLocationRelativeTo(null);//창이 가운데 나오게
	        setResizable(true);
	        setBackground(Color.white);
	        setVisible(true);
		}
	}
	
	public static void main(String[] args) {
		new CustomerSystem();
	}
}
