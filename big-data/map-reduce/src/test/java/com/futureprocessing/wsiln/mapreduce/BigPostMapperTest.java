package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import com.futureprocessing.wsiln.tools.TimeCounter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BigPostMapperTest {

    @Test
    @Ignore
    public void shouldMapBigPostUnder100Miliseconds() throws IOException {
        //given
        Text input = new Text(BIG_ROW);
        //when


        //then
        TimeCounter timer = new TimeCounter("testTimer").start();
        DateTime start = new DateTime();
        MapDriver<LongWritable, Text, RelationKey, MappingType> mapDriver;

        LongWritable inputKey = new LongWritable(1l);

        for (int i = 0; i < 100000; i++) {
//            TechnologiesMapper technologiesMapper = new TechnologiesMapper();
//            technologiesMapper.map();

            mapDriver = new MapDriver<LongWritable, Text, RelationKey, MappingType>()
                    .withMapper(new TechnologiesMapper())
                    .withInput(new LongWritable(1l), input);
            mapDriver.run();
            mapDriver = null;
        }

        //then

        DateTime end = new DateTime();
        Duration duration = new Duration(start, end);

        timer.end();
        assertThat(duration.getMillis()).isLessThan(100);
    }


    private static final String BIG_ROW = "<row Id=\"21\" PostTypeId=\"2\" ParentId=\"9\" CreationDate=\"2008-08-01T08:57:27.280\" Score=\"14\" Body=\"&lt;p&gt;Many years ago, to provide an &lt;a href=&quot;http://cloud9.hedgee.com/age&quot;&gt;age calculator gimmick&lt;/a&gt; on my website, I wrote a function to calculate age to a fraction. This is a quick port of that function to C# (from &lt;a href=&quot;http://hedgee.svn.sourceforge.net/viewvc/hedgee/trunk/chris/ckwww/ckage.php3&quot;&gt;the PHP version&lt;/a&gt;). I'm afraid I haven't been able to test the C# version, but hope you enjoy all the same!&lt;/p&gt;&#xD;&#xA;&#xD;&#xA;&lt;p&gt;(Admittedly this is a bit gimmicky for the purposes of showing user profiles on Stack Overflow, but maybe readers will find some use for it. :-))&lt;/p&gt;&#xD;&#xA;&#xD;&#xA;&lt;pre&gt;&lt;code&gt;double AgeDiff(DateTime date1, DateTime date2) {&lt;br&gt;    double years = date2.Year - date1.Year;&lt;br&gt;&lt;br&gt;    /*&lt;br&gt;     * If date2 and date1 + round(date2 - date1) are on different sides&lt;br&gt;     * of 29 February, then our partial year is considered to have 366&lt;br&gt;     * days total, otherwise it's 365. Note that 59 is the day number&lt;br&gt;     * of 29 Feb.&lt;br&gt;     */&lt;br&gt;    double fraction = 365&lt;br&gt;            + (DateTime.IsLeapYear(date2.Year) &amp;amp;&amp;amp; date2.DayOfYear &amp;gt;= 59&lt;br&gt;            &amp;amp;&amp;amp; (date1.DayOfYear &amp;lt; 59 || date1.DayOfYear &amp;gt; date2.DayOfYear)&lt;br&gt;            ? 1 : 0);&lt;br&gt;&lt;br&gt;    /*&lt;br&gt;     * The only really nontrivial case is if date1 is in a leap year,&lt;br&gt;     * and date2 is not. So let's handle the others first.&lt;br&gt;     */&lt;br&gt;    if (DateTime.IsLeapYear(date2.Year) == DateTime.IsLeapYear(date1.Year))&lt;br&gt;        return years + (date2.DayOfYear - date1.DayOfYear) / fraction;&lt;br&gt;&lt;br&gt;    /*&lt;br&gt;     * If date2 is in a leap year, but date1 is not and is March or&lt;br&gt;     * beyond, shift up by a day.&lt;br&gt;     */&lt;br&gt;    if (DateTime.IsLeapYear(date2.Year)) {&lt;br&gt;        return years + (date2.DayOfYear - date1.DayOfYear&lt;br&gt;                - (date1.DayOfYear &amp;gt;= 59 ? 1 : 0)) / fraction;&lt;br&gt;    }&lt;br&gt;&lt;br&gt;    /*&lt;br&gt;     * If date1 is not on 29 February, shift down date1 by a day if&lt;br&gt;     * March or later. Proceed normally.&lt;br&gt;     */&lt;br&gt;    if (date1.DayOfYear != 59) {&lt;br&gt;        return years + (date2.DayOfYear - date1.DayOfYear&lt;br&gt;                + (date1.DayOfYear &amp;gt; 59 ? 1 : 0)) / fraction;&lt;br&gt;    }&lt;br&gt;&lt;br&gt;    /*&lt;br&gt;     * Okay, here date1 is on 29 February, and date2 is not on a leap&lt;br&gt;     * year. What to do now? On 28 Feb in date2's year, the ``age''&lt;br&gt;     * should be just shy of a whole number, and on 1 Mar should be&lt;br&gt;     * just over. Perhaps the easiest way is to a point halfway&lt;br&gt;     * between those two: 58.5.&lt;br&gt;     */&lt;br&gt;    return years + (date2.DayOfYear - 58.5) / fraction;&lt;br&gt;}&lt;br&gt;&lt;/code&gt;&lt;/pre&gt;\" OwnerUserId=\"13\" LastEditorUserId=\"13\" LastEditorDisplayName=\"Chris Jester-Young\" LastEditDate=\"2008-08-01T12:35:11.707\" LastActivityDate=\"2008-08-01T12:35:11.707\" CommentCount=\"1\" CommunityOwnedDate=\"2011-08-16T19:40:43.080\" />";

}

