package p1;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.*;


public class FiveChessFrame extends JFrame implements MouseListener{
	//get the width of screen		
	int width = Toolkit.getDefaultToolkit().getScreenSize().width; 
	//height
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	BufferedImage bgImage = null;
	//the position of stones
	int begin=1;
	int rr=0;
	int x=0;
	int y=0; 
	int wx=0;
	int wy=0;
	int key=1;
	int lian=0;
	int [] bx=new int [20],by=new int [20];
	int bn=0;
	int dn=0;
	int num=0;
	int hnum=0;
	int [] hx=new int [361],hy=new int [361];
	int ub1,db1,ub2,db2;
	//save the stones' position
	//0 = not taken, 1 = black, 2 = white
	int [][] allChess = new int [19][19],analyse=new int [19][19],landform=new int [19][19],winw=new int[19][19],winb=new int[19][19];
	//which should do next
	boolean isBlack = true;
	
	boolean canPlay = true;
	
	public FiveChessFrame()//the main frame
	{
		this.setTitle("this is not Gomoku");
		this.setSize(500, 500);
		this.setLocation((width - 500)/2, (height - 500)/2);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.addMouseListener(this);
		int k,l;
		for(int q=0;q<=18;q++){//make each position to gain value
			for(int p=0;p<=18;p++){
				if(q-9<0)
					k=9-q;
				else
					k=q-9;
				if(p-9<0)
					l=9-p;
				else
					l=p-9;
				landform[q][p]=18-k-l;
				canPlay=false;
			}
		}
		
		
		//init background
		try {
			bgImage = ImageIO.read(new File("image/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void paint(Graphics g)//
	{
		//Double buffering technique to prevent screen flicker
		BufferedImage bi = new BufferedImage(500, 525, BufferedImage.TYPE_INT_ARGB);//8 bit RGBA
		Graphics g2 = bi.createGraphics(); 
		g2.drawImage(bgImage, 3, 25, this );
		g2.setColor(Color.BLACK);
		
		g2.setFont(new Font("Times New Roman", Font.BOLD, 25));
		g2.setFont(new Font("Times New Roman", 0 , 14));
		
		for(int i=0; i<=18; i++)
		{
			g2.drawLine(13, 75+20*i, 372, 75+20*i);
			g2.drawLine(13+20*i, 76, 13+20*i, 434);
		}
		
		g2.fillOval(71, 133, 4, 4);
		g2.fillOval(311, 133, 4, 4);
		g2.fillOval(311, 373, 4, 4);
		g2.fillOval(71, 373, 4, 4);
		g2.fillOval(71, 253, 4, 4);
		g2.fillOval(311, 253, 4, 4);
		g2.fillOval(191, 133, 4, 4);
		g2.fillOval(191, 373, 4, 4);
		g2.fillOval(191, 253, 4, 4);

		
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
			{
				if(allChess[i][j]==1)
				{
					//black stone
					int tempX = i * 20 + 13;
					int tempY = j * 20 + 76;
					g2.fillOval(tempX-7, tempY-7, 14, 14);
				}
				if(allChess[i][j]==2)
				{
					//white stone
					int tempX = i * 20 + 13;
					int tempY = j * 20 + 76;
					g2.setColor(Color.WHITE);
					g2.fillOval(tempX-7, tempY-7, 14, 14);
					g2.setColor(Color.BLACK);
					g2.drawOval(tempX-7, tempY-7, 14, 14);
				}
			}
		
		g.drawImage(bi, 0, 0, this);
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void boundary(int [] a,int flag){//looking for boundary
		int m=4;
		while(true){
			if(a[m+1]==flag)
				m++;
			else{
				ub1=m;
				break;
			}
			if(m==8){
				ub1=m;
				break;
			}
		}
		m=4;
		while(true){
			if(a[m-1]==flag)
				m--;
			else{
				db1=m;
				break;
			}
			if(m==0){
				db1=m;
				break;
			}
		}
		m=4;
		while(true){
			if(a[m+1]==0|a[m+1]==flag)
				m++;
			else{
				ub2=m;
				break;
			}
			if(m==8){
				ub2=8;
				break;
			}
		}
		m=4;
		while(true){
			if(a[m-1]==0||a[m-1]==flag)
				m--;
			else{
				db2=m;
				break;
			}
			if(m==0){
				db2=0;
				break;
			}
		}
	}
	public int shape(int[] a,int flag){//evaluate the level of current situation
		if(ub2-db2<4)
			return 0;
		if(ub1-db1==4){
			return 11;
		}
		else if(ub1-db1==3){
			if(ub1<ub2&&db1>db2)
				return 10;
			else if(ub1==ub2&&db1==db2)
				return 0;
			else
				return 9;
		}
		else if(ub1-db1==2){
			if(ub1<ub2&&db1>db2){
				if(ub1+2<=8){
					if(a[ub1+2]==flag)
						return 8;
				}
				if(db1-2>=0){
					if(a[db1-2]==flag)
						return 8;
				}
				return 7;
			}
			else if(ub1==ub2&&db1!=db2){
				if(db1-2>=0)
					if(a[db1-2]==flag)
						return 6;
			}
			else if(ub1!=ub2&&db1==db2){
				if(ub1+2<=8)
					if(a[ub1+2]==flag)
						return 6;
			}
			else
				return 0;
		}
		else if(ub1-db1==1){
			if(ub1<ub2&&db1>db2){
				if(ub1+3<=8)
					if(a[ub1+2]==flag&&a[ub1+3]==flag)
						return 5;
				if(db1-3>=0)
					if(a[db1-2]==flag&&a[db1-3]==flag)
						return 5;
				if(ub1+2<=8)
					if(a[ub1+2]==flag)
						return 4;
				if(db1-2>=0)
					if(a[db1-2]==flag)
						return 4;
			}
			if(ub1==ub2&&db1!=db2){
				if(db1-3>=0)
					if(a[db1-2]==flag&&a[db1-3]==flag)
						return 5;
				if(db1-2>=0)
					if(a[db1-2]==flag)
						return 3;
			}
			if(ub1!=ub2&&db1==db2){
				if(ub1+3<=8)
					if(a[ub1+2]==flag&&a[ub1+3]==flag)
						return 5;
				if(ub1+2<=8)
					if(a[ub1+2]==flag)
						return 3;
			}
			return 0;
		}
		else if(ub1-db1==0){
			if(ub2==8)
				if(a[ub2]==flag&&a[ub2-1]==flag&&a[ub2-2]==flag)
					return 2;
				else if(a[ub2]==0&&a[ub2-1]==flag&&a[ub2-2]==flag)
					return 1;
			if(db2==0)
				if(a[db2]==flag&&a[db2+1]==flag&&a[db2+2]==flag)
					return 2;
				else if(a[db2]==0&&a[db2+1]==flag&&a[db2+2]==flag)
			        return 1;
		}
		return 0;
	}
	public void first_s(int A,int B,int [][] a,int flag){
		int [] F=new int [4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		   if(F[i]==11){
			   wx=A;wy=B;
			   allChess[A][B]=2;
			   key=0;
			   break;
		   }
		}
	}
	public void second_s(int A,int B,int [][] a,int flag){
		int [] F=new int [4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		   if(F[i]==11){
			   wx=A;wy=B;
			   allChess[A][B]=2;
			   key=0;
			   break;
		   }
		}
	}
	public void third_s(int A,int B,int [][] a,int flag){
		int [] F=new int [4];
		int [][] b=new int[4][9];
		int n=-1,sum=0;
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		   if(F[i]==10){
			  n=i; 
			 }
		   sum=sum+F[i];
		}
		if(n==0&&B+5<=18){
			b=estimate(A,B+5,flag);
			if(third_s_s(A,B+5,b,flag)<=sum){
				wx=A;wy=B;
				allChess[A][B]=2;
				key=0;
			}
			else{
				wx=A;wy=B+5;
				allChess[A][B+5]=2;
				key=0;
			}
		}
		else if(n==1&&A+5<=18){
			b=estimate(A+5,B,flag);
			if(third_s_s(A+5,B,b,flag)<=sum){
				wx=A;wy=B;
				allChess[A][B]=2;
				key=0;
			}
			else{
				wx=A+5;wy=B;
				allChess[A+5][B]=2;
				key=0;
			}
		}
		else if(n==2&&A+5<=18&&B+5<=18){
			b=estimate(A+5,B+5,flag);
			if(third_s_s(A+5,B+5,b,flag)<=sum){
				wx=A;wy=B;
				allChess[A][B]=2;
				key=0;
			}
			else{
				wx=A+5;wy=B+5;
				allChess[A+5][B+5]=2;
				key=0;
			}
		}
		else if(n==3&&A+5<=18&&B-5>=0){
			b=estimate(A+5,B-5,flag);
			if(third_s_s(A+5,B-5,b,flag)<=sum){
				wx=A;wy=B;
				allChess[A][B]=2;
				key=0;
			}
			else{
				wx=A+5;wy=B-5;
				allChess[A+5][B-5]=2;
				key=0;
			}
		}
	}
	public void fourth_s(int A,int B,int [][] a,int flag){
		int [] F=new int [4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		   if(F[i]==10){
			   wx=A;wy=B;
			   allChess[A][B]=2;
			   key=0;
			   break;
		   }
		}
	}
	public void fifth_s(int A,int B,int [][] a,int flag){
		int [] F=new int [4];
		int double_s=0;
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		}
		for(int i=0;i<=3;i++){
			if(F[i]==9||F[i]==8||F[i]==7||F[i]==6||F[i]==5||F[i]==4||F[i]==2||F[i]==1)
				double_s++;
		}
		if(double_s>=2){
			wx=A;wy=B;
			allChess[A][B]=2;
			key=0;
		}
	}
	public void sixth_s(int A,int B,int [][] a,int flag){
		int [] F=new int [4];
		int double_s=0;
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		}
		for(int i=0;i<=3;i++){
			if(F[i]==9||F[i]==8||F[i]==7||F[i]==6||F[i]==5||F[i]==4||F[i]==2||F[i]==1)
				double_s++;
		}
		if(double_s>=2){
			wx=A;wy=B;
			allChess[A][B]=2;
			key=0;
		}
	}
	public void seventh_s(int A,int B,int [][] a,int flag){
		int []F=new int[4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		}
		for(int i=0;i<=3;i++){
			if(F[i]>=6){
				wx=A;wy=B;
				allChess[A][B]=2;
				key=0;
			}
		}
	}
	public void eighth_s(int A,int B,int [][] a,int flag){
		int []F=new int[4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		}
		for(int i=0;i<=3;i++){
			if(F[i]>3){
				wx=A;wy=B;
				allChess[A][B]=2;
				key=0;
			}
		}
	}
	public void ninth_s(int A,int B,int [][] a,int flag){
		int []F=new int[4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		}
		for(int i=0;i<=3;i++){
			winb[A][B]+=F[i];
		}
	}
	public void tenth_s(int A,int B,int [][] a,int flag){
		int []F=new int[4];
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		}
		for(int i=0;i<=3;i++){
			winb[A][B]+=F[i];
		}
	}
	public int third_s_s(int A,int B,int [][] a,int flag){
		int []F=new int[4];
		int sum=0,key=0;
		for(int i=0;i<=3;i++){
		   boundary(a[i],flag);
		   F[i]=shape(a[i],flag);
		   sum=sum+F[i];
		   if(F[i]==10)
			   key=1;
		}
		if(key==1)
		   return sum;
		else
			return 0;
	}
	public void find(){//Can iterate through all the points and analysis respectively, choose reasonable move later
		int i=0,j=0,breakk=0;
		while(true){
			while(analyse[i][j]!=0){
				i++;
				if(i==19){
					i=0;
					j++;
				}
				if(j==19){
					breakk=1;
					break;
				}
			}
			if(breakk==1)
				break;
			
			if(num%2==0)
			  estimate(i,j,1);
			else
				estimate(i,j,2);
			if(key==0)
				break;
			analyse[i][j]=0;
			i++;
			if(i==19){
				i=0;
				j++;
			}
			if(j==19)
				break;
		}
	}
	public void Gomoku(){//main function of analyzing
		Random ran=new Random();
		int i=0,j=0,breakk=0,max=-1000,mx=0,my=0;
		int []nx=new int[20],ny=new int[20];
		while(true){
		     num++;
		     find();
		     if(key==0){
			    break;
		     }
		     if(num==10)
		    	 break;
		}
		if(key==1||num==10){
		for(i=0;i<=18;i++){
			for(j=0;j<=18;j++){
				if(allChess[i][j]==0){
			       winw[i][j]-=winb[i][j];
			       winw[i][j]+=landform[i][j];
			       if(max<winw[i][j]){
				      max=winw[i][j];
				      mx=1;my=1;
				      nx[mx-1]=i;ny[my-1]=j;
			       }
			       else if(max==winw[i][j]){
			    	   mx++;my++;
			    	   nx[mx-1]=i;ny[my-1]=j;
			       }
			   }
			}
		}
		int t=ran.nextInt(mx);
		wx=nx[t];wy=ny[t];
		allChess[wx][wy]=2;
		}
		for(i=0;i<=18;i++)
			for(j=0;j<=18;j++){
				winw[i][j]=winb[i][j]=0;
			}
		key=1;num=0;
	}
	public int[][] estimate(int i,int j,int flag){//choose the most reasonable move
		double sum=0,sa,sb,sc,sd;
		int vx=0,vy=0;
		int [][] a=new int [4][9];
		for(vy=-4;vy<=4;vy++){
			if(j+vy<0||j+vy>18)
				a[0][4+vy]=-1;
			else if(vy==0)
				a[0][4+vy]=flag;
			else if(analyse[i][j+vy]==1)
				a[0][4+vy]=1;
			else if(analyse[i][j+vy]==2)
				a[0][4+vy]=2;
			else
				a[0][4+vy]=0;
		}
		for(vx=-4;vx<=4;vx++){
			if(i+vx<0||i+vx>18)
				a[1][4+vx]=-1;
			else if(vx==0)
				a[1][4+vx]=flag;
			else if(analyse[i+vx][j]==1)
				a[1][4+vx]=1;
			else if(analyse[i+vx][j]==2)
				a[1][4+vx]=2;
			else
				a[1][4+vx]=0;
		}
		for(vx=-4;vx<=4;vx++){
			if(j+vx<0||j+vx>18||i+vx<0||i+vx>18)
				a[2][4+vx]=-1;
			else if(vx==0)
				a[2][4+vx]=flag;
			else if(analyse[i+vx][j+vx]==1)
				a[2][4+vx]=1;
			else if(analyse[i+vx][j+vx]==2)
				a[2][4+vx]=2;
			else
				a[2][4+vx]=0;
		}
		for(vx=-4;vx<=4;vx++){
			if(j-vx<0||j-vx>18||i+vx<0||i+vx>18)
				a[3][4+vx]=-1;
			else if(vx==0)
				a[3][4+vx]=flag;
			else if(analyse[i+vx][j-vx]==1)
				a[3][4+vx]=1;
			else if(analyse[i+vx][j-vx]==2)
				a[3][4+vx]=2;
			else
				a[3][4+vx]=0;
		}
		if(num==1)
		  first_s(i,j,a,2);
		else if(num==2)
		    second_s(i,j,a,1);
		else if(num==3)
			third_s(i,j,a,2);
		else if(num==4)
			fourth_s(i,j,a,1);
		else if(num==5)
			fifth_s(i,j,a,2);
		else if(num==6)
			sixth_s(i,j,a,1);
		else if(num==7)
			seventh_s(i,j,a,2);
		else if(num==8)
			eighth_s(i,j,a,1);
		else if(num==9)
			ninth_s(i,j,a,2);
		else if(num==10)
			tenth_s(i,j,a,1);
		return a;
	}
	public void mousePressed(MouseEvent e) {//listen click
		x=e.getX();
		y=e.getY();
		if(x>16&&x<89&&y>447&&y<488){//
			if(canPlay==false){
			   canPlay=true;
			   for(int i=0;i<=18;i++)
					 for(int j=0;j<=18;j++)
						 allChess[i][j]=0;
			   int result=JOptionPane.showConfirmDialog(this, "start first");
			   if(result==1){
				 allChess[9][9]=2;
				 hx[hnum]=9;
			     hy[hnum]=9;
				 hnum++;
				 isBlack=true;
			   }
			   else if(result==2)
				   canPlay=false;
			   else
				   isBlack=true;
			}
			else{
				int result = JOptionPane.showConfirmDialog(this, "restart");
				if(result==0){
					for(int i=0;i<=18;i++)
						 for(int j=0;j<=18;j++)
							 allChess[i][j]=0;
					int result1 = JOptionPane.showConfirmDialog(this, "start first");
					   if(result1==1){
						 allChess[9][9]=2;
						 hx[hnum]=9;
					     hy[hnum]=9;
						 hnum++;
						 isBlack=true;
					   }
					   else
						   isBlack=true;
				}
			}
		}
		this.repaint();
		if(x>105&&x<178&&y>447&&y<488){//undo last move
			if(allChess[hx[0]][hy[0]]==2){
				if(hnum>1){
					allChess[hx[hnum-1]][hy[hnum-1]]=0;
					allChess[hx[hnum-2]][hy[hnum-2]]=0;
					hnum=hnum-2;
					canPlay=true;
				}
				else
					JOptionPane.showMessageDialog(this,"you do not move");
			}
			else{
				if(hnum>0){
					allChess[hx[hnum-1]][hy[hnum-1]]=0;
					allChess[hx[hnum-2]][hy[hnum-2]]=0;
					hnum=hnum-2;
					canPlay=true;
				}
				else
					JOptionPane.showMessageDialog(this,"you do not move");
			}
		}
		this.repaint();
		if(x>=13&&x<=372&&y>=76&&y<=434&&canPlay==true){//move or action
			x=Math.round((float)(x-13)/20);
			y=Math.round((float)(y-76)/20);
			if(allChess[x][y]==0){
			  if(isBlack==true){
				  allChess[x][y]=1;
				  hx[hnum]=x;
				  hy[hnum]=y;
				  hnum++;
			  }
			  boolean winFlagB = this.checkWin(x,y);
			  if(winFlagB){
				  JOptionPane.showMessageDialog(this, "Game over, black win");
				  canPlay = false;
			  }
			  for(int i=0;i<=18;i++)
				   for(int j=0;j<=18;j++)
					  analyse[i][j]=allChess[i][j];//analyse[i][j]
			  Gomoku();
			  hx[hnum]=wx;
			  hy[hnum]=wy;
			  hnum++;
			  winFlagB=this.checkWin(wx,wy);
			  if(winFlagB){
				  JOptionPane.showMessageDialog(this, "Game over, white win");
				  canPlay = false;
			  }
			}
		}
		this.repaint();
		if(x>191&&x<263&&y>447&&y<488){//surrender
			for(int i=0;i<=18;i++)
				 for(int j=0;j<=18;j++)
					 allChess[i][j]=0;
			canPlay=false;
		}
		this.repaint();
		if(x>277&&x<352&&y>447&&y<488){//exit
			int reslut=JOptionPane.showConfirmDialog(this, "exit£¿");
			if(reslut==0)
			  System.exit(0);
		}
		this.repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean checkWin(int a,int b){//win or not
	
//		
		int count = 1;
		boolean flag = false;
		
		// y from allChess[x][y] is the same?
		int color = allChess[a][b];

		
		count = checkCount(1, 0, color,a,b);
		if(count >= 5)
			flag = true;
		else
		{
			
			count = checkCount(0, 1, color,a,b);
			if(count >= 5)
				flag = true;
			else
			{
				
				count = checkCount(1, -1, color,a,b);
				if(count >= 5)
					flag = true;
				else
				{
					
					count = checkCount(1, 1, color,a,b);
					if(count >= 5)
						flag = true;
				}
			}
		}
		return flag;
	}
	
	
	private int checkCount(int xChange, int yChange, int color,int x,int y){//how much stones are in a lane
	
		int count = 1;
		int xTemp = xChange;
		int yTemp = yChange;
		while(x + xChange >=0 && x+ xChange <= 18 && y + yChange >=0 && y + yChange <= 18 && color == allChess[x + xChange][y + yChange])
		{
			count++;
			if(xChange!=0)
				xChange++;
			if(yChange!=0)
			{
				if(yChange > 0)
					yChange++;
				else yChange--;
			}
		}
		xChange = xTemp;
		yChange = yTemp;
		while(x - xChange >=0 && x - xChange <= 18 && y - yChange >=0 && y - yChange <= 18 && color == allChess[x - xChange][y - yChange])
		{
			count++;
			if(xChange!=0)
				xChange++;
			if(yChange!=0)
			{
				if(yChange > 0)
					yChange++;
				else yChange--;
			}
		}
		return count;
	}

  
}
