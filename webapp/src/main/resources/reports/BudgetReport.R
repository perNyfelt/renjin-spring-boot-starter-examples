
lineItems <- data.frame(
  name = character(),
  jan = numeric(),
  feb = numeric(),
  mar = numeric(),
  apr = numeric(),
  may = numeric(),
  jun = numeric(),
  jul = numeric(),
  aug = numeric(),
  sep = numeric(),
  oct = numeric(),
  nov = numeric(),
  dec = numeric(),
  stringsAsFactors = FALSE
)

# Hardcoded stuff for local testing
#lineItems[1, ] <- list("Software", -1200, 0, 0 ,0 , -400, -600, 0, -100, -50, -200, -80, 0 )
#lineItems[2, ] <- list("Computer hardware", 0, 0, -33000 , 0 , 0, 0, 0, 0, -50000, 0, 0, 0 )
#lineItems[3, ] <- list("Admin staff", -7000, -7000, -7000 , -7000, -7000, -7000, -7000, -7000, -7000, -7000, -7000, -7000 )
#lineItems[4, ] <- list("Dev staff", -12000, -12000, -12000 , -12000, -12000, -12000, -12000, -18000, -18000, -18000, -18000, -18000 )
#lineItems[5, ] <- list("Sales", 22000, 22000, 42000 , 25000, 26000, 27000, 28000, 29000, 60000, 31000, 32000, 33000 )


index <- 1
for (item in items) {
  #print(paste(item$getName(), item$getMonthlyCosts()))
  lineItems[index, ] <- c(item$getName(), item$getMonthlyCosts())
  index <- index + 1;
}

lineItems[nrow(lineItems) +1 ,] <- c("Total", colSums(lineItems[, -1]))
# we can bind variables so that they can be retrieved with getVariable from the Environment from the scriptEngine
assign("summaryTable", lineItems, .GlobalEnv)


monthsMatrix <- as.matrix(lineItems[nrow(lineItems), -1])

outFile <- tempfile("plot", fileext = ".png")
png(outFile)
barplot(monthsMatrix, main="Monthly results"
         ,,, col="blue4"
        #, legend = rownames(lineItems$name)

)
dev.off()
# The last thing is the return value of the script
outFile






