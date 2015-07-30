package lia.analysis.positional;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.CharArraySet;
import java.io.IOException;

// From chapter 4
public class PositionalStopFilter extends TokenFilter {
  private CharArraySet stopWords;
  private PositionIncrementAttribute posIncrAttr;
  private TermAttribute termAttr;

  public PositionalStopFilter(TokenStream in, CharArraySet stopWords) {
    super(in);
    this.stopWords = stopWords;
    posIncrAttr = (PositionIncrementAttribute) addAttribute(PositionIncrementAttribute.class);
    termAttr = (TermAttribute) addAttribute(TermAttribute.class);
  }

  public final boolean incrementToken() throws IOException {
    int increment = 0;
    while(input.incrementToken()) {
      if (!stopWords.contains(termAttr.termBuffer(), 0, termAttr.termLength())) {
        posIncrAttr.setPositionIncrement(posIncrAttr.getPositionIncrement() + increment);
        return true;
      }

      increment += posIncrAttr.getPositionIncrement();
    }

    return false;
  }
}
