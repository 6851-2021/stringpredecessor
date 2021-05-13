#include <cmath>
#include <cstdio>
#include <iostream>
#include <stack>
#include <climits>
#include <algorithm>

using namespace std;

int trie[N][26] = {0};
int end[N] = {0};
int numofnodes = 0;

void insert(string s){
    int curnode = 0;
    for(int i = 0; i < s.size(); i++){
        if(trie[curnode][s[i]-'a'] == 0){
            trie[curnode][s[i]-'a'] = ++numofnodes;
        }
        curnode = trie[curnode][s[i]-'a'];
    }
    end[curnode]++;
}

bool search(string s){
    int curnode = 0;
    for(int i = 0; i < s.size(); i++){
        if(trie[curnode][s[i]-'a'] == 0){
            return false;
        }
        curnode = trie[curnode][s[i]-'a'];
    }
    return end[curnode] > 0;
}

int main(int argc, const char * argv[]) {
    insert("hello");
    insert("heyyo");
    cout<<search("heyyo")<<endl;
    
    for(int i = 0; i < 8; i++){
        for(int j = 0; j < 26; j++){
            cout<<trie[i][j]<<" ";
        }
        cout<<endl;
    }
    
    return 0;
}