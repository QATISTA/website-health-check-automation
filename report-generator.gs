function insertScreenshotsAndUrls() {
  var doc = DocumentApp.getActiveDocument();
  var body = doc.getBody();
 
  // Folder & file IDs
  var folder = DriveApp.getFolderById("1vsOCAQHQRd3CQj046zW3OPzWHkmR9Kl1");
  var file = DriveApp.getFileById("1FVzXmR9bqIjE1SjiG-IIzmg4LHv9vyo8");




  // Read URLs
  var urlsContent = file.getBlob().getDataAsString();
  var urls = urlsContent.split(/\r?\n/);




  // Collect screenshots
  var filesIter = folder.getFiles();
  var screenshots = [];
  while (filesIter.hasNext()) {
    screenshots.push(filesIter.next());
  }




  // Sort by filename
  screenshots.sort(function(a, b) {
    return a.getName().localeCompare(b.getName());
  });




  // Insert URL + Screenshot at the end
  for (var i = 0; i < urls.length && i < screenshots.length; i++) {
    var urlText = urls[i].trim();
    if (urlText) {
      body.appendParagraph(urlText)
          .setFontFamily("Verdana")
          .setFontSize(11)
          .setBold(false)  
          .setForegroundColor("#000000");  // 🔹 Force black text
    }




    var img = screenshots[i].getBlob();
    var insertedImg = body.appendImage(img);




var maxWidth = 700; // adjust for your doc
var originalWidth = insertedImg.getWidth();
var originalHeight = insertedImg.getHeight();




if (originalWidth > maxWidth) {
  var scale = maxWidth / originalWidth;
  insertedImg.setWidth(maxWidth).setHeight(originalHeight * scale);
}
    // 🔹 Set fixed width and height
    //insertedImg.setWidth(600).setHeight(300);




    body.appendParagraph(""); // spacing
  }
}











