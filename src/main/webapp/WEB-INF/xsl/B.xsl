<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html>
<body>
<h3> 這是一篇好文章阿</h3>

<table>
<tbody>
 <tr bgcolor="#9acd32">
 <th style="text-align:left">Title</th>
  <th style="text-align:left">Artist</th>
   <th style="text-align:left">Title</th>
  <th style="text-align:left">Artist</th>
  </tr>
   <xsl:for-each select="document/comments">
    <tr>
      <td><xsl:value-of select="id"/></td>
      <td><xsl:value-of select="comments"/></td>
      <td><xsl:value-of select="who/nickname"/></td>
      <td><xsl:value-of select="who/id"/></td>
    </tr>
    
    </xsl:for-each>
    <xsl:for-each select="document">
        <tr> <td><xsl:value-of select="postedAt"/></td>
  <td><xsl:value-of select="nickname"/></td>
  <td><xsl:value-of select="id"/></td>
     <td><xsl:value-of select="content"/></td></tr>
    </xsl:for-each>


   

  </tbody> 
</table>
</body>
</html>

</xsl:template>
</xsl:stylesheet >
