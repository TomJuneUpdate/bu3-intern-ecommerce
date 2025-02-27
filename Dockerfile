FROM openjdk17
LABEL authors="admin"

ENTRYPOINT ["top", "-b"]