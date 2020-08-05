package snake;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
public class Paint extends JPanel implements KeyListener, ActionListener{
	ImageIcon title;
	ImageIcon up;
	ImageIcon left;
	ImageIcon right;    //����ͼƬ
	ImageIcon down ;
	ImageIcon body; 
	
	int len =3;	 //��ʼ����
	int score = 0;//��ʼ����
	String header = "R";     //ͷ������
	boolean isStarted = false;  //�ж�״̬
	boolean isFailed = false;
	javax.swing.Timer timer = new javax.swing.Timer(150,this);  //��ʱ�{
	int[] locX = new int[820];		//����������������
	int[] locY = new int[820];
	int shitX;          //ʳ������
	int shitY;
	Random rand = new Random();   //�����
	Clip bgm;  //������������
	
	public Paint() {
		loadImages();  //����ͼƬ
		initSnake();//��ʼ��
		this.setFocusable(true);
		this.addKeyListener(this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);      //���û���
		this.setBackground(Color.green);   //���ñ�����ɫ
		title.paintIcon(this, g, 25, 11);     //����title
		
		g.fillRect(25, 75, 850, 600);    //��Ϸ��
		g.setColor(Color.red);
		g.setFont(new Font("",Font.BOLD,15));
		g.drawString("���� "+ len, 550, 30);   //��ӡ���ȷ���
		g.drawString("ս��ֵ " + score, 550, 55);
		switch(header){				//��ӡͷ��
		case "L":
			left.paintIcon(this, g, locX[0], locY[0]);
			break;
		case "R":
			right.paintIcon(this, g, locX[0], locY[0]);
			break;
		case "U":
			up.paintIcon(this, g, locX[0], locY[0]);
			break;
		case "D":
			down.paintIcon(this, g, locX[0], locY[0]);
			break;
		}
		for(int i=1; i<len; i++) {			//��ӡ����
			body.paintIcon(this, g, locX[i], locY[i]);
		}
		body.paintIcon(this, g, shitX, shitY);  //��ӡʳ��
		if(! isStarted) {
			g.setColor(Color.white);		//��������
			g.setFont(new Font("",Font.BOLD,30));
			g.drawString("����enter�����ϰ�,��������", 300, 330);
		}
		if(isFailed) {
			g.setColor(Color.red);		//��������
			g.setFont(new Font("arial",Font.BOLD,35));
			g.drawString("RIP", 400, 330);
		}
	}
	
	public void initSnake() {					//��ʼ��
		len = 3;
		locX[0] = 100;
		locY[0] = 100;
		locX[1] = 75;
		locY[1] = 100;
		locX[2] = 50;
		locY[2] = 100;
		shitX = 25 + 25*rand.nextInt(34);
		shitY = 75 + 25*rand.nextInt(24);
		header = "R";
		score =0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER) {
			if(isFailed) {
				isFailed = false;    //���¿�ʼ��Ϸ
				initSnake();
			}else {
			isStarted = !isStarted; //��ͣ����
			}
			repaint();			//enter�����ػ�1
		}else if(keyCode == KeyEvent.VK_LEFT) {
			header = "L";
		}else if(keyCode == KeyEvent.VK_RIGHT) {
			header = "R";
		}
		else if(keyCode == KeyEvent.VK_UP) {
			header = "U";
		}else if(keyCode == KeyEvent.VK_DOWN) {
			header = "D";
		}
;		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(isStarted && !isFailed) {
			for(int i=len-1; i>0; i--) {		//����������ƶ�
				locX[i] =locX[i-1];
				locY[i] = locY[i-1];
				
			}
			if(header == "R") {
				locX[0] += 25;		//����ͷ���ƶ�
				if(locX[0] > 850)
					locX[0] = 25;
			}else if(header == "L") {
				locX[0] -= 25;
				if(locX[0] < 25)
					locX[0] = 850;
			}else if(header == "U") {
				locY[0] -= 25;
				if(locY[0] < 75 )
					locY[0] = 650;
			}else if(header == "D") {
				locY[0] += 25;
				if(locY[0] > 650)
					locY[0] = 75;
			}
			
			if(locX[0] == shitX && locY[0] == shitY) {
				loadBGM();  //��������
				playBGM();
				len++;  //����+1���ɣ��߼�Ϊ�������ǰһ��������Ҫ�����趨����
				score += 5;
				locX[len-1] = locX[len-2];
				locY[len-1] = locY[len-2];
				shitX = 25 + 25*rand.nextInt(34);
				shitY = 75 + 25*rand.nextInt(24);
			}
			
			for(int i=1; i<len; i++) {
				if(locX[i] == locX[0] && locY[i] == locY[0])
					isFailed = true;
			}
			repaint();
		}
		timer.start();
		
	}

	private void loadBGM() {		//��������
			try {
				bgm = AudioSystem.getClip();
				InputStream s = this.getClass().getClassLoader().getResourceAsStream("music/olegy.wav");
				AudioInputStream ss = AudioSystem.getAudioInputStream(s);
				bgm.open(ss);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private void playBGM() {
		bgm.start();
	}
	
	private void loadImages() {  //����ͼƬ
		InputStream s;
		try {
		s = getClass().getClassLoader().getResourceAsStream("pic/title8.png");
		title = new ImageIcon(ImageIO.read(s));
		s = getClass().getClassLoader().getResourceAsStream("pic/up.png");
		up = new ImageIcon(ImageIO.read(s));
		s = getClass().getClassLoader().getResourceAsStream("pic/left.png");
		left = new ImageIcon(ImageIO.read(s));
		s = getClass().getClassLoader().getResourceAsStream("pic/right.png");
		right = new ImageIcon(ImageIO.read(s));
		s = getClass().getClassLoader().getResourceAsStream("pic/down.png");
		down = new ImageIcon(ImageIO.read(s));
		s = getClass().getClassLoader().getResourceAsStream("pic/body.png");
		body = new ImageIcon(ImageIO.read(s));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
