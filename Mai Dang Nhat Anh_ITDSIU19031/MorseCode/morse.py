import time
import pyttsx3 as pyttsx
from pydub import AudioSegment
from pydub.playback import play

MORSE_CODE_DICT = dict({ ' ':'/', 'A':'.-', 'B':'-...', 'C':'-.-.', 'D':'-..', 'E':'.', 'F':'..-.', 'G':'--.', 'H':'....',
                    'I':'..', 'J':'.---', 'K':'-.-', 'L':'.-..', 'M':'--', 'N':'-.','O':'---', 'P':'.--.', 'Q':'--.-',
                    'R':'.-.', 'S':'...', 'T':'-','U':'..-', 'V':'...-', 'W':'.--','X':'-..-', 'Y':'-.--', 'Z':'--..',
                    '1':'.----', '2':'..---', '3':'...--','4':'....-', '5':'.....', '6':'-....','7':'--...', '8':'---..', '9':'----.',
                    '0':'-----', ', ':'--..--', '.':'.-.-.-','?':'..--..', '/':'-..-.', '-':'-....-','(':'-.--.', ')':'-.--.-'})


def encode(txt):
    code = [MORSE_CODE_DICT[i.upper()] + ' ' for i in txt if i.upper() in MORSE_CODE_DICT.keys()]
    morse=''.join(code)
    for m in morse:
        if m=='.':
            song = AudioSegment.from_mp3("D:\IU\DataSecurity\Midterm\Mai Dang Nhat Anh_ITDSIU19031\MorseCode\dot.wav")
            play(song)
        elif m=='-':
            song1 = AudioSegment.from_mp3("D:\IU\DataSecurity\Midterm\Mai Dang Nhat Anh_ITDSIU19031\MorseCode\dash.wav")
            play(song1)
        else:
            time.sleep(0.5)
    return morse
def decode(txt):
    code = [k for i in txt.split() for k,v in MORSE_CODE_DICT.items() if i==v]
    newtxt = ''.join(code)
    engine = pyttsx.init()
    engine.say(newtxt)
    engine.runAndWait()
    return newtxt

def main():
    decode()
if __name__ == '__main__':
    main()