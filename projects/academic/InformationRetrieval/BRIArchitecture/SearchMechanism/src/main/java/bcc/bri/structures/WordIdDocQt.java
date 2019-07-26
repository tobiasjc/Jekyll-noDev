/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcc.bri.structures;

import java.io.Serializable;

/**
 * @author flavio
 */
public class WordIdDocQt implements Comparable, Serializable {
    //id da palavra
    public int wordId;
    //quantos documentos possuem essa palavra
    public int docQt;

    public WordIdDocQt() {
    }

    public WordIdDocQt(int wordId, int docQt) {
        this.wordId = wordId;
        this.docQt = docQt;
    }

    public int getWordId() {
        return wordId;
    }

    public int getDocQt() {
        return docQt;
    }

    @Override
    public int compareTo(Object o) {
        WordIdDocQt pd = (WordIdDocQt) o;
        if (this.getDocQt() < pd.getDocQt())
            return -1;

        return 1;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        WordIdDocQt pd = (WordIdDocQt) o;
        // field comparison
        return (this.getDocQt() == pd.getDocQt()
                && this.getWordId() == pd.getWordId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.wordId;
        return hash;
    }
}
