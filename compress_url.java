package compress_url2;
import java.util.*;
import java.io.*;
//node structure
class node
{
 int cp;//common prefix
 int id;
 int height;
 String diffUrl;// different url
 node parent;//address of parent
 node left;//address of left child
 node right;//address of right child
 public node(int i)
 {
 cp=0;
 id=i;
 parent=null;
 left=null;
 right=null;
 height=1;
 }
}
class avl
{
//Calculate height of the avl tree
 int height(node N)
 {
 if (N == null)
 return 0;
 return N.height;
 }
//calculate balance of the node
 int getBalance(node N) {
 if (N == null)
 return 0;
 return height(N.left) - height(N.right);
 }
//function to find common prefix length between two strings
 int getprefix(String A,String B)
 {
 int la=A.length();
 int lb=B.length();
 int c=Math.min(la,lb);
 for(int i=0;i<c;i++)
 {
 if(A.charAt(i)!=B.charAt(i))
 return i;
 }
 return c;
 }
// INSERTION
 node insert(node root,node a,String URL)
 {
 //WHEN ROOT==NULL
 if(root==null)
 {
 a.diffUrl=URL.substring(a.cp);
 return a;
 }
 //CALLING RETRIEVE()
 String rURL=retrieve(root);
 int c=rURL.compareTo(URL);
 //WHEN NODE IS ALREADY PRESENT
 if(c==0)
 return root;
 a.cp=getprefix(rURL,URL);
 a.parent=root;
 if(c<0)// GO right
 {
 root.right=insert(root.right,a,URL);
 }
 else// GO left
 {
 root.left=insert(root.left,a,URL);
 }
 root.height = 1 + Math.max(height(root.left),height(root.right));
 int balance = getBalance(root);
 String rlurl=retrieve(root.left);
 String rrurl=retrieve(root.right);
 // Left Left Case
 //if (balance > 1 && key < root.left.key)
 if(balance >1 && URL.compareTo(rlurl)<0)
 return rightRotate(root);
 // Right Right Case
 //if (balance < -1 && key > root.right.key)
 if(balance <-1 && URL.compareTo(rrurl)>0)
 return leftRotate(root);
 // Left Right Case
 // if (balance > 1 && key > root.left.key)
 if(balance > 1&& URL.compareTo(rlurl)>0)
 {
 root.left = leftRotate(root.left);
 return rightRotate(root);
 }

 // Right Left Case
 //if (balance < -1 && key < root.right.key)
 if(balance <-1 && URL.compareTo(rrurl)<0)
 {
 root.right = rightRotate(root.right);
 return leftRotate(root);
 }
 return root;
 }
 //LEFT_ROTATION
 node leftRotate(node x)
 {
 String xurl=retrieve(x);
 node y=x.right;
 String yurl=retrieve(y);
 y.parent=x.parent;
 x.parent=y;
 x.right=y.left;
 y.left=x;
 //CALCULATE HEIGHT
 x.height = Math.max(height(x.left), height(x.right)) + 1;
 y.height = Math.max(height(y.left), height(y.right)) + 1;
 //CALL RECALC() FOR Y AND X
 y=recalc(y,yurl);
 x=recalc(x,xurl);
 return y;
 }
 //RIGHT_ROTATION
 node rightRotate(node x)
 {
 String xurl=retrieve(x);
 node y=x.left;
 String yurl=retrieve(y);
 y.parent=x.parent;
 x.parent=y;
 x.left=y.right;
 y.right=x;
 //CALCULATE HEIGHT
 x.height = Math.max(height(x.left), height(x.right)) + 1;
 y.height = Math.max(height(y.left), height(y.right)) + 1;
 //CALL RECALC() FOR Y AND X
 y=recalc(y,yurl);
 x=recalc(x,xurl);
 return y;
 }
 //RECALCULATE COMMON PREFIX AND DIFFERENT URL
 node recalc(node a,String url)
 {
 String purl=retrieve(a.parent);
 a.cp=getprefix(purl,url);
 a.diffUrl=url.substring(a.cp);
 return a;
 }
 //SEARCHING
 node search(node root,String URL)
 {
 if(root==null)
 return null;
 String temp=retrieve(root);
 int c=temp.compareTo(URL);
 if(c==0)
 {
 return root;
 }
 if(c<0)//go right
 {
 return search(root.right,URL);
 }
 //go left
 return search(root.left,URL);
 }
 //RETREIVING
 String retrieve(node a)
 {
 if(a==null)
 return "";
 if(a.cp==0)
 return a.diffUrl;
 String t=(retrieve(a.parent)).substring(0,a.cp)+a.diffUrl;

 return t;
 }
}
public class compress_url
{
 public static void main(String args[])throws IOException
 {
 BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
 avl tree1=new avl();
 node root=null;
 ArrayList<node> indexes =new ArrayList<node>();
 String URL;
 int id=0;
 for(;;)//INFINITE LOOP FOR CHOICE
 {
 System.out.println("Enter choice");
 System.out.println("Enter 1 for insert");
 System.out.println("Enter 2 for search");
 System.out.println("Enter 3 for retrieval");
 System.out.println("Total url stored ="+indexes.size());
 int ch=Integer.parseInt(br.readLine());
 switch(ch)
 {
 case 1://INSERTION
 System.out.println("Enter the URL you want to store");

 URL=br.readLine();
 node a=new node(id);
 indexes.add(id,a);
 id++;
 root=tree1.insert(root,a,URL);
 System.out.println("Common prefix ="+a.cp);
 System.out.println("Different URL ="+a.diffUrl);
 break;
 case 2://SEARCHING
 System.out.println("Enter the URL you want to search");
 URL=br.readLine();
 node b=tree1.search(root,URL);
 if(b==null)
 System.out.println("Not found");
 else
 System.out.println("Found at index "+b.id);
 System.out.println("Common prefix ="+b.cp);
 System.out.println("Different URL ="+b.diffUrl);
 break;
 case 3: //RETRIEVAL
 System.out.println("Enter the index of the node you want to retrieve");
 int i=Integer.parseInt(br.readLine());
 node c=indexes.get(i);
 String url=tree1.retrieve(c);
 System.out.println(url);
 break;
 default:
 System.out.println("Ending.....");
 System.exit(0);
 }
 }
 }
}

