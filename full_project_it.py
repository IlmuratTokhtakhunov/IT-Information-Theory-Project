# -*- coding: utf-8 -*-
import copy
import random

# ---------------------- Functions ------------------------

def split_str(seq, chunk, skip_tail=False):
    lst = []
    if chunk <= len(seq):
        lst.extend([seq[:chunk]])
        lst.extend(split_str(seq[chunk:], chunk, skip_tail))
    elif not skip_tail and seq:
        lst.extend([seq])
    return lst


def inversebit(instr, index):
  new_str = list(instr) # makes a list from our string
  if new_str[index] == '1':
      new_str[index] = '0'
  else:
      new_str[index] = '1'

  new_str = ''.join(new_str)
  return new_str


def fileopen(name, op, outstr = None):
    if op == 'r':
        infile = open(name, op)
        outstr = infile.read()
        infile.close()
        return outstr
    elif op == 'w':
        outfile = open(name, op)
        outfile.write(outstr)
        outfile.close()
        return True


# ---------------------------------------------------------

con = 1
char_list =[]
dictlist = []
err_table = ['101', '111', '110', '011', '100', '010', '001']


text = fileopen('text.txt', 'r')
text = text.upper()
length = len(text)
ortext = text

# ---------------------- Main Part ---------------------------

# --- PART 1

while len(text) != 0:
    ch = text[0]
    con = 1
    for j in range(1, len(text)-1):
        if ch == text[j: j+1]:
            con += 1
    text = text.replace(ch, '')
    char_list.append({'char': ch, 'abs_prob': con/length, 'rel_prob': "{}/{}".format(con, length)})

for c in char_list:
    fileopen('dictionary.txt', 'a', c['char'] + ' - ' + str(c['abs_prob']))

print('PART 1')
for u in char_list:
    print(u)

# --- PART 2

print("\n\nPART 2")
list1 = copy.deepcopy(char_list)

for z in range(0, len(char_list) - 1):
    for x in range(0, len(char_list)):
        for j in range(x + 1, len(char_list)):
            if(char_list[j]['abs_prob'] < char_list[x]['abs_prob']):
                temp = char_list[x]
                char_list[x] = char_list[j]
                char_list[j] = temp

    char_list[0]['char'] = '(' + char_list[0]['char'] + '|' + char_list[1]['char'] + ')'
    char_list[0]['abs_prob'] = char_list[0]['abs_prob'] + char_list[1]['abs_prob']
    print('Node ' + char_list[0]['char'] + ', probability: {}'.format(char_list[0]['abs_prob']))
    char_list.pop(1)

res = char_list[0]['char']
for i in list1:
    for z in range(0, len(res)):
        if i['char'] == res[z]:
            cod = ''
            lborder = z
            rborder = z
            while True:
                if res[lborder-1] == '(':
                    lborder = lborder - 1
                else:
                    cod = cod + '0'
                    boromir = 0
                    for l in range(lborder-1, -1, -1):
                        if res[l] == '(' and boromir == 0:
                            lborder = l
                            break
                        if res[l] == ')':
                            boromir = boromir+1
                        if res[l] == '(':
                            boromir = boromir-1
                if res[rborder+1] == ')':
                    rborder = rborder + 1
                else:
                    cod = cod + '1'
                    boromir = 0
                    for l in range(rborder + 1, len(res)):
                        if res[l] == ')' and boromir == 0:
                            rborder = l
                            break
                        if res[l] == '(':
                            boromir = boromir + 1
                        if res[l] == ')':
                            boromir = boromir - 1
                if rborder == len(res)-1 and lborder == 0:
                    break
            dictlist.append({'char': i['char'], 'code': cod[::-1]})

print('\nDictionary:\n')
for u in dictlist:
    print(u)

res = ''
for i in range(len(ortext)):
    for u in dictlist:
        if ortext[i] == u['char']:
            res = res + u['code']

print('\n\nOriginal text: ' + ortext)
print('\nEncrypted text: ' + res)


# --- PART 3
# we use only dictionary list(dictlist) and binary sequence(res), result is original text(res2)

def decrypt(binseq):
    res2 = ''
    bpos = 0
    for i in range(len(res)+1):
        for j in dictlist:
            if binseq[bpos:i] == j['code']:
                res2 = res2 + j['char']
                bpos = i
    return res2

result = decrypt(res)
print('\n\nPART 3')
print('Decrypted text: ' + result)

fileopen('part3result.txt', 'w', result)


# --- PART 4
# we use only binary sequence(res) created after 2 Part.

z1 = len(res)%4
if z1 != 0:
    print('\n\nzuji')
    c = 4 - z1
    for i in range(c):
        res = res + '0'

res4 = ''
for i in range(len(res)):
    res4 = res4 + res[i]
    if (i+1) % 4 == 0:
        res4 = res4 + str((int(res[i-3]) ^ int(res[i-2]) ^ int(res[i-1])))
        res4 = res4 + str((int(res[i-2]) ^ int(res[i-1]) ^ int(res[i])))
        res4 = res4 + str((int(res[i-3]) ^ int(res[i-2]) ^ int(res[i])))
        
print('\n\nPART 4')
print('Result encoded using Hamming code (7,4): '+res4)
fileopen('part4result.txt', 'w', res4)


# --- PART 5

print("\n\nPART 5")
text = fileopen('part4result.txt', 'r')
split_text = split_str(text, 7)
print('splitted: ')
print(split_text)

for i in range(len(split_text)):
    rand_j = random.randint(0, 6)
    split_text[i] = inversebit(split_text[i], rand_j)
print('splitted with errors: ')
print(split_text)

output = ''.join(split_text)
fileopen('part5result.txt', 'w', output)


# --- PART 6

print("\n\nPART 6")
bits = fileopen('part5result.txt', 'r')
split_bits = split_str(bits, 7)

for i in range(len(split_bits)):
  elem = split_bits[i]
  s1 = int(elem[4]) ^ int(elem[0]) ^ int(elem[1]) ^ int(elem[2])
  s2 = int(elem[5]) ^ int(elem[1]) ^ int(elem[2]) ^ int(elem[3])
  s3 = int(elem[6]) ^ int(elem[0]) ^ int(elem[1]) ^ int(elem[3])
  err = str(s1) + str(s2) + str(s3)
  
  for k in range(len(err_table)):
    if err_table[k] == err:
      elem = inversebit(elem, k)
  
  split_bits[i] = elem[:4]

print('Splitted without errors: ')
print(split_bits)
print()

split_bits = ''.join(split_bits)
result = decrypt(split_bits)
print('Decrypted text: ' + result)

fileopen('part6result.txt', 'w', 'Encrypted:\n'+ split_bits +'\n\nDecrypted:\n'+ result)