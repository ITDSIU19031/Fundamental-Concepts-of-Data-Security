from tkinter import *
from PIL import Image, ImageTk
from morse import encode, decode


root = Tk()
root.title('Mai Dang Nhat Anh_ITDSIU19031')
root.geometry("500x630")

load = Image.open('D:\IU\DataSecurity\Midterm\Mai Dang Nhat Anh_ITDSIU19031\MorseCode\dg.png')
render = ImageTk.PhotoImage(load)
img = Label(root, image = render)
img.place(x = 0, y = 0)

name = Label(root, text = 'Morse', fg = "#000000", bd = 0)
name.config(font = ('',30))
name.pack(pady = 10)


box = Text(root, width = 28, height = 8, font = ("ROBOTO", 16))
box.pack(pady = 20)

def encrypt():
    Input = box.get(1.0, END)
    a = encode(Input)
    box1.insert(END, a)
def decrypt():
    Input = box.get(1.0, END)
    a = decode(Input)
    box1.insert(END, a)

button_frame = Frame(root).pack(side = BOTTOM)
encode_button = Button(button_frame, text = 'Encrypt', font= (('Arial'),10,'bold'), bg = '#303030', fg='#FFFFFF', command = encrypt)
encode_button.place(x = 150, y =310)
decode_button = Button(button_frame, text = 'Decrypt', font= (('Arial'),10,'bold'), bg = '#303030', fg='#FFFFFF', command = decrypt)
decode_button.place(x = 290, y =310)

box1 = Text(root, width = 28, height = 8, font = ("ROBOTO", 16))
box1.pack(pady = 50)


root.mainloop()