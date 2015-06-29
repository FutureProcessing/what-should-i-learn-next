package com.futureprocessing.wsiln.mapreduce;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class InputForrmaterText {

    @Test
    public void shouldSplitTagsInputString() {
        //given
        String tags = "&lt;wi-fi&gt;&lt;standby&gt;";
        //when

        String[] result = InputFormatter.splitInputString(tags);
        String[] shouldBe = {"wi-fi", "standby"};
        //then
        assertThat(result).isEqualTo(shouldBe);
    }

    @Test
    public void shouldSplitPostInputString() {
        //given
        String post = "&lt;&gt;When I am downloading large files I need to stop my phone from sleeping&lt;&gt;";
        //when

        String[] result = InputFormatter.splitInputString(post);
        String[] shouldBe = {"when", "i", "am", "downloading", "large", "files", "i", "need", "to", "stop", "my", "phone", "from", "sleeping"};
        //then
        assertThat(result).isEqualTo(shouldBe);

    }
}
